package pl.wszib.booksyprojekt2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wszib.booksyprojekt2.client.BooksyClient;
import pl.wszib.booksyprojekt2.entity.WatchRequest;
import pl.wszib.booksyprojekt2.repository.WatchRequestRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PollingService {
    private static final Logger log = LoggerFactory.getLogger(PollingService.class);

    private final WatchRequestRepository repository;
    private final BooksyClient client;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    //booksy.poll-delay-ms - zależność z application.properties
    @Scheduled(fixedDelayString = "${booksy.poll-delay-ms}")
    @Transactional
    public void poll() {
        List<WatchRequest> active = repository.findByNeedToFindNewDatesTrue();
        if (active.isEmpty()) return;
        log.debug("Polling {} watch(es) w poszukiwaniu wolnych terminów...", active.size());
        for (WatchRequest wr : active) {
            try {
                var result = client.fetchTimeSlots(
                        wr.getBusinessId(),
                        wr.getServiceVariantId(),
                        wr.getStafferId(),
                        wr.getStartDate(),
                        wr.getEndDate()
                );
                wr.setLastCheckedAt(Instant.now());
                wr.setLastResponse(result.rawJson());
                if (result.needToFindNewSlots()) {
                    wr.setNeedToFindNewDates(false);
                    log.info("Znaleziono dostępne terminy dla watchId={} (daty {} -> {})",
                            wr.getId(), wr.getStartDate(), wr.getEndDate());
                    System.out.println("===================================");
                    System.out.println("Znaleziono termin poprzez poszukiwanie w tle!");
                    JsonNode node = objectMapper.readTree(wr.getLastResponse());
                    JsonNode timeSlots = node.path("time_slots");
                    JsonNode firstDay = timeSlots.get(0);
                    String date = firstDay.path("date").asText();
                    String time = firstDay
                            .path("slots")
                            .get(0)
                            .path("t")
                            .asText();
                    System.out.println(date + " " + time);
                    emailService.sendSimpleMessage(wr.getEmail(), "Znaleziono termin na Booksy!", "Cześć!\nAplikacja Booksy-project znalazła dla Ciebie termin na usługę między " + wr.getStartDate() + " a " + wr.getEndDate() + ".\nLeć się zapisać!");
                }
                repository.save(wr);
            } catch (Exception e) {
                log.warn("Błąd podczas odpytywania Booksy dla watchId={}: {}", wr.getId(), e.getMessage());
            }
        }
    }
}

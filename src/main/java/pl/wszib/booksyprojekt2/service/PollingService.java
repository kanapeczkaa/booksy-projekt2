package pl.wszib.booksyprojekt2.service;

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

    //booksy.poll-delay-ms (domyślnie 60s) - zależność z application.properties
    @Scheduled(fixedDelayString = "${booksy.poll-delay-ms}")
    @Transactional
    public void poll() {
        System.out.println("*uruchomiono metodę poll w PollingService");
        List<WatchRequest> active = repository.findByFoundAtLeastOneFalse();
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
                if (result.hasSlots()) {
                    wr.setFoundAtLeastOne(true);
                    log.info("Znaleziono dostępne terminy dla watchId={} (daty {} -> {})",
                            wr.getId(), wr.getStartDate(), wr.getEndDate());
                    System.out.println("===================================");
                    System.out.println("Znaleziono termin poprzez poszukiwanie w tle!");
                    System.out.println(wr.getLastResponse());
                }
                repository.save(wr);
            } catch (Exception e) {
                log.warn("Błąd podczas odpytywania Booksy dla watchId={}: {}", wr.getId(), e.getMessage());
            }
        }
    }
}

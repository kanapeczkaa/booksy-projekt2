package pl.wszib.booksyprojekt2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wszib.booksyprojekt2.client.BooksyClient;
import pl.wszib.booksyprojekt2.config.BooksyProperties;
import pl.wszib.booksyprojekt2.dto.CreateWatchRequestDto;
import pl.wszib.booksyprojekt2.entity.WatchRequest;
import pl.wszib.booksyprojekt2.repository.WatchRequestRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchService {
    private final WatchRequestRepository repository;
    private final BooksyProperties props;
    private final BooksyClient booksyClient;

    @Transactional
    public WatchRequest createAndCheck(CreateWatchRequestDto dto) {
        WatchRequest wr = create(dto);
        wr = check(wr);
        return repository.save(wr);
    }

    @Transactional
    public WatchRequest create(CreateWatchRequestDto dto) {
        WatchRequest wr = new WatchRequest();
        wr.setStartDate(dto.getStartDate());
        wr.setEndDate(dto.getEndDate());
        wr.setServiceVariantId(dto.getServiceVariantId() != null ? dto.getServiceVariantId()
                : props.getDefaultServiceVariantId());
        wr.setStafferId(dto.getStafferId() != null ? dto.getStafferId() : props.getDefaultStafferId());
        wr.setBusinessId(dto.getBusinessId() != null ? dto.getBusinessId() : props.getDefaultBusinessId());
        wr.setRequestedAt(Instant.now());
        wr.setFoundAtLeastOne(false);
        return repository.save(wr);
    }
    
    public WatchRequest check(WatchRequest wr){
        var result = booksyClient.fetchTimeSlots(
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
        }
        return wr;
    }

    /**
     * Tworzy watcha i od razu odpytuje Booksy. Zapisuje surową odpowiedź i
     * ustawia flagę foundAtLeastOne, jeśli znaleziono wolne terminy.
     */


    @Transactional(readOnly = true)
    public WatchRequest get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("WatchRequest not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<WatchRequest> getAll() {
        return repository.findAll();
    }
}

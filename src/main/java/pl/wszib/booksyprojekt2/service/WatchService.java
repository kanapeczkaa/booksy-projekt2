package pl.wszib.booksyprojekt2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wszib.booksyprojekt2.client.BooksyClient;
import pl.wszib.booksyprojekt2.config.BooksyProperties;
import pl.wszib.booksyprojekt2.controller.WatchController;
import pl.wszib.booksyprojekt2.dto.CreateWatchRequestDto;
import pl.wszib.booksyprojekt2.entity.WatchRequest;
import pl.wszib.booksyprojekt2.repository.WatchRequestRepository;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchService {
    private final WatchRequestRepository repository;
    private final BooksyProperties props;
    private final BooksyClient booksyClient;
    private final ObjectMapper objectMapper;

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
        wr.setEmail(dto.getEmail() != null ? dto.getEmail() : props.getDefaultEmail());
        wr.setEmail(dto.getEmail());
        wr.setRequestedAt(Instant.now());
        wr.setNeedToFindNewDates(true);
        System.out.println("Nowy WatchRequest:\n" + wr);
        return repository.save(wr);
    }

    public WatchRequest check(WatchRequest wr) {
        var result = booksyClient.fetchTimeSlots(
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
        }
        return wr;
    }
    
    @Transactional(readOnly = true)
    public WatchRequest get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("WatchRequest not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<WatchRequest> getAll() {
        return repository.findAll();
    }

    
}

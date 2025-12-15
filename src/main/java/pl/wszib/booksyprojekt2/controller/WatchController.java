package pl.wszib.booksyprojekt2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wszib.booksyprojekt2.dto.CreateWatchRequestDto;
import pl.wszib.booksyprojekt2.dto.WatchDto;
import pl.wszib.booksyprojekt2.dto.WatchSlotsResponseDto;
import pl.wszib.booksyprojekt2.entity.WatchRequest;
import pl.wszib.booksyprojekt2.service.WatchService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/watches")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    @PostMapping
    public ResponseEntity<WatchSlotsResponseDto> create(@RequestBody @Valid CreateWatchRequestDto dto) {
        // Utwórz watcha i od razu wykonaj pierwsze sprawdzenie
        WatchRequest wr = watchService.createAndCheck(dto);
        WatchSlotsResponseDto out = new WatchSlotsResponseDto(
                wr.getId(),
                wr.getFoundAtLeastOne(),
                wr.getLastResponse()
        );
        // Jeśli od razu są wolne terminy – zwróć 200 OK z listą/JSONem slotów
        if (out.isHasAny()) {
            System.out.println("===================================");
            System.out.println("Znaleziono termin za pierwszym razem!");
            return ResponseEntity.ok(out);
        }
        System.out.println("===================================");
        System.out.println("Nie znaleziono terminów!");
        // Jeśli jeszcze brak terminów – zwróć 201 Created + Location i body z hasAny=false
        return ResponseEntity.created(URI.create("/api/watches/" + wr.getId())).body(out);
    }

    @GetMapping
    public List<WatchDto> list() {
        return watchService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WatchDto get(@PathVariable Long id) {
        return toDto(watchService.get(id));
    }

    @GetMapping("/{id}/slots")
    public WatchSlotsResponseDto slots(@PathVariable Long id) {
        WatchRequest wr = watchService.get(id);
        return new WatchSlotsResponseDto(wr.getId(), wr.getFoundAtLeastOne(), wr.getLastResponse());
    }
    
    //mapowanie, przeniesc to do config
    private WatchDto toDto(WatchRequest wr) {
        return new WatchDto(
                wr.getId(),
                wr.getStartDate(),
                wr.getEndDate(),
                wr.getServiceVariantId(),
                wr.getStafferId(),
                wr.getBusinessId(),
                wr.getRequestedAt(),
                wr.getLastCheckedAt(),
                wr.getFoundAtLeastOne()
        );
    }
}

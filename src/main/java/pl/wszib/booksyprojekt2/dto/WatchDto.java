package pl.wszib.booksyprojekt2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long serviceVariantId;
    private Long stafferId;
    private Long businessId;
    private Instant requestedAt;
    private Instant lastCheckedAt;
    private Boolean foundAtLeastOne;
}

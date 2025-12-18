package pl.wszib.booksyprojekt2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "watch_requests")
@ToString
public class WatchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long serviceVariantId;
    private Long stafferId;
    private Long businessId;
    
    private String email;

    @Column(nullable = false, updatable = false)
    private Instant requestedAt;

    private Instant lastCheckedAt;

    private Boolean needToFindNewDates;

    @Lob
    private String lastResponse;
}

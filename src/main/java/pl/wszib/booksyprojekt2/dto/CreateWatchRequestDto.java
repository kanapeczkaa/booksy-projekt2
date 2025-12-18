package pl.wszib.booksyprojekt2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class CreateWatchRequestDto {

    @NotNull
    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @JsonProperty("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonProperty("service_variant_id")
    private Long serviceVariantId;

    @JsonProperty("staffer_id")
    private Long stafferId;

    @JsonProperty("business_id")
    private Long businessId;
    
    private String email;
}

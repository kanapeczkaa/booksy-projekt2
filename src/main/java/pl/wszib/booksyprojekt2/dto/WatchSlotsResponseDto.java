package pl.wszib.booksyprojekt2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WatchSlotsResponseDto {
    private Long id;
    private boolean hasAny;
    private String rawJson;
}

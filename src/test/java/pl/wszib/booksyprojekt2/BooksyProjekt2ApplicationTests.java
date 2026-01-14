package pl.wszib.booksyprojekt2;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import pl.wszib.booksyprojekt2.controller.WatchController;
import pl.wszib.booksyprojekt2.dto.CreateWatchRequestDto;
import pl.wszib.booksyprojekt2.dto.WatchSlotsResponseDto;

import java.time.LocalDate;

@SpringBootTest
class BooksyProjekt2ApplicationTests {

    @Autowired
    private WatchController watchController;

    @Test
    void contextLoads() {
    }
                                                                                                                                                

    @Test
    public void shouldReturnWrongParameters() {
        System.out.println("poniżej zwraca błąd wpisanych danych dot. salonu.");
        CreateWatchRequestDto requestDto = CreateWatchRequestDto.builder()
                .businessId(9999L)
                .serviceVariantId(9999L)
                .stafferId(9999L)
                .email("goradominik99@gmail.com")
                .startDate(LocalDate.of(2026, 01, 30))
                .endDate(LocalDate.of(2026, 02, 28))
                .build();

        ResponseEntity<WatchSlotsResponseDto> result = watchController.create(requestDto);
        System.out.println("-------------------------------");
        System.out.println(result.toString());
        System.out.println("-------------------------------");
    }

    @Test
    public void shouldReturnFreeDates() {
        System.out.println("Poniżej zwraca poprawnie terminy.");
        CreateWatchRequestDto requestDto = CreateWatchRequestDto.builder()
                .businessId(326496L)
                .serviceVariantId(21158492L)
                .stafferId(-1L)
                .email("goradominik99@gmail.com")
                .startDate(LocalDate.of(2026, 02, 01))
                .endDate(LocalDate.of(2026, 02, 3))
                .build();
        ResponseEntity<WatchSlotsResponseDto> result = watchController.create(requestDto);
        
        System.out.println("-------------------------------");
        System.out.println(result.toString());
        System.out.println("-------------------------------");
    }
    
    @Test
    public void shouldReturnNoDates(){
        System.out.println("Poniżej zwraca brak terminów.");
        CreateWatchRequestDto requestDto = CreateWatchRequestDto.builder()
                .businessId(326496L)
                .serviceVariantId(21158492L)
                .stafferId(-1L)
                .email("goralplayer@gmail.com")
                .startDate(LocalDate.of(2026, 01, 24))
                .endDate(LocalDate.of(2026, 01, 24))
                .build();
        ResponseEntity<WatchSlotsResponseDto> result = watchController.create(requestDto);
        
        System.out.println("-------------------------------");
        System.out.println(result.toString());
        System.out.println("-------------------------------");
    }

}

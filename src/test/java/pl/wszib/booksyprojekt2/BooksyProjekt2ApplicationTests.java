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

    //    @BeforeAll
//    public static void init() {
//        System.out.println("proba");
//        ConfigurableApplicationContext ctx = SpringApplication.run(BooksyProjekt2Application.class);
//        
//        watchController = ctx.getBean(WatchController.class);
//        //MockitoAnnotations.openMocks(this);
//    }
    @BeforeAll
    public static void init() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }


    @Test
    public void shouldReturnWrongParameters() {
        System.out.println("poniżej zwraca błąd wpisanych danych dot. salonu.");
        CreateWatchRequestDto requestDto = CreateWatchRequestDto.builder()
                .businessId(9999L)
                .serviceVariantId(9999L)
                .stafferId(9999L)
                .email("goradominik99@gmail.com")
                .startDate(LocalDate.of(2026, 01, 01))
                .endDate(LocalDate.of(2026, 01, 30))
                .build();
//        System.out.println(requestDto);

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
                .startDate(LocalDate.of(2026, 01, 01))
                .endDate(LocalDate.of(2026, 01, 5))
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
                .email("goradominik99@gmail.com")
                .startDate(LocalDate.of(2026, 01, 04))
                .endDate(LocalDate.of(2026, 01, 4))
                .build();
        ResponseEntity<WatchSlotsResponseDto> result = watchController.create(requestDto);
        System.out.println("-------------------------------");

        System.out.println(result.toString());
        System.out.println("-------------------------------");
    }

}

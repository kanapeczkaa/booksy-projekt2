package pl.wszib.booksyprojekt2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import pl.wszib.booksyprojekt2.controller.WatchController;
import pl.wszib.booksyprojekt2.dto.CreateWatchRequestDto;
import pl.wszib.booksyprojekt2.dto.WatchSlotsResponseDto;
import pl.wszib.booksyprojekt2.service.WatchService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

@SpringBootApplication
public class BooksyProjekt2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BooksyProjekt2Application.class, args);

//        WatchController watchController = ctx.getBean(WatchController.class);
//
//        if (Files.exists(new File("src/main/resources/data.json").toPath())) {
//            System.out.println("plik istnieje!");
//            watchController.readDataFromFileAndStart("src/main/resources/data.json");
//        }
//        

//        ObjectMapper objectMapper = ctx.getBean(ObjectMapper.class);
//        
//        WatchController watchController = ctx.getBean(WatchController.class);
//        try {
//            CreateWatchRequestDto dto = objectMapper.readValue(new File("src/main/resources/data.json"), CreateWatchRequestDto.class);
//            System.out.println(dto.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        //watchController.create()
//
//        System.out.println("Poniżej zwraca brak terminów.");
//        CreateWatchRequestDto requestDto = CreateWatchRequestDto.builder()
//                .businessId(9999L)
//                .serviceVariantId(9999L)
//                .stafferId(9999L)
//                .startDate(LocalDate.of(2026, 01, 01))
//                .endDate(LocalDate.of(2026, 01, 30))
//                .build();
//        ResponseEntity<WatchSlotsResponseDto> result = watchController.create(requestDto);
//        System.out.println("-------------------------------");
//
//        System.out.println(result.toString());
//        System.out.println("-------------------------------");
    }

}

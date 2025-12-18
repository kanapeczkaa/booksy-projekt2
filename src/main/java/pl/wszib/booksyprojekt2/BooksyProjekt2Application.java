package pl.wszib.booksyprojekt2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.wszib.booksyprojekt2.controller.WatchController;


@SpringBootApplication
public class BooksyProjekt2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BooksyProjekt2Application.class, args);

        WatchController watchController = ctx.getBean(WatchController.class);
        watchController.readDataFromFileAndStart("src/main/resources/data.json");
    }

}

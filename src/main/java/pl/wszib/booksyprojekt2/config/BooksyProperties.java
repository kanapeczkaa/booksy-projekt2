package pl.wszib.booksyprojekt2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "booksy")
public class BooksyProperties {
    /** Np. https://pl.booksy.com/core/v2/customer_api */
    private String baseUrl;
    private String apiKey;

    private Long defaultBusinessId;
    private Long defaultServiceVariantId;
    private Long defaultStafferId;

    private long pollDelayMs = 60000;
}

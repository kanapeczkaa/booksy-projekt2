package pl.wszib.booksyprojekt2.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import pl.wszib.booksyprojekt2.config.BooksyProperties;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BooksyClient {
    private static final Logger log = LoggerFactory.getLogger(BooksyClient.class);

    private final RestTemplate restTemplate;
    private final BooksyProperties props;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Result fetchTimeSlots(Long businessId,
                                 Long serviceVariantId,
                                 Long stafferId,
                                 LocalDate startDate,
                                 LocalDate endDate) {
        String url = props.getBaseUrl() + "/me/businesses/" + businessId + "/appointments/time_slots";

        Map<String, Object> subbooking = new HashMap<>();
        subbooking.put("service_variant_id", serviceVariantId);
        subbooking.put("staffer_id", stafferId);

        Map<String, Object> body = new HashMap<>();
        body.put("subbookings", List.of(subbooking));
        body.put("start_date", startDate.toString());
        body.put("end_date", endDate.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        headers.set("x-api-key", props.getApiKey());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            String raw = response.getBody();
            boolean hasSlots = false;
            try {
                if (raw != null && !raw.isBlank()) {
                    JsonNode root = objectMapper.readTree(raw);
                    hasSlots = detectSlots(root);
                }
            } catch (Exception e) {
                log.warn("Nie udało się zinterpretować odpowiedzi Booksy: {}", e.getMessage());
            }
            
            return new Result(raw, hasSlots, response.getStatusCode().value());
        } catch (RestClientResponseException ex) {
            String errorBody = ex.getResponseBodyAsString();
            log.warn("Błąd odpowiedzi Booksy (status {}): {}", ex.getRawStatusCode(), errorBody);
            return new Result(errorBody, true, ex.getRawStatusCode());
        }
    }
    
    //sprawdza, czy API Booksy zwróciło wolne terminy, czy pustą tablicę
    private boolean detectSlots(JsonNode node) {
        System.out.println("*odpalenie metody sprawdzającej Json z api booksy");
        if (node.isObject()) {
            JsonNode ts = node.get("time_slots");
            if (ts != null && ts.isArray() && ts.size() > 0) return true;
//            usunąć, to probowalem robic aby przestawić flagę dla żądania z błędnymi danymi
//            System.out.println("~~~~~~~~");
//            System.out.println(ts.get(0).toString());
        } 
        return false;
    }

    public record Result(String rawJson, boolean needToFindNewSlots, int statusCode) {}
}
                                                                                                                                                                        

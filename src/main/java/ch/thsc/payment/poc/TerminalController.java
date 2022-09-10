package ch.thsc.payment.poc;

import ch.thsc.payment.poc.bo.Authorization;
import ch.thsc.payment.poc.bo.Terminal;
import ch.thsc.payment.poc.bo.Transaction;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
//@ConditionalOnExpression("${termianl.controller.enabled}")
public class TerminalController {

    private final String TERMINAL_ID = "TID1234";
    private final String MERCHANT = "Merchant 1234";

    private Logger logger = LoggerFactory.getLogger(TerminalController.class);

    private List<Transaction> transactions = new ArrayList<>();

    private boolean initOk = false;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @PostMapping("/terminal/init")
    public void getInit() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:8080/management/init/" + TERMINAL_ID;
        ResponseEntity<Terminal> response = restTemplate.getForEntity(fooResourceUrl, Terminal.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            initOk = true;
            logger.info("Terminal [{}] initialized: [{}]", TERMINAL_ID, ReflectionToStringBuilder.toString(response, ToStringStyle.MULTI_LINE_STYLE, true, true));
        }
    }

    @RequestMapping(value = "/terminal/pay/{card}/{amount}", method = RequestMethod.POST)
    public void pay(@PathVariable String card, @PathVariable Integer amount) {
        if (!initOk) {
            terminalWasNotInitialized();
        }

            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl = "http://localhost:8080/gateway/authorize/" + TERMINAL_ID + "/" + card + "/" + amount;
            ResponseEntity<Authorization> response = restTemplate.getForEntity(fooResourceUrl, Authorization.class);

            transactions.add(Transaction.builder()
                    .merchantName(MERCHANT)
                    .authorizationReferenceNumber(response.getBody().getAuthorizationReferenceNumber())
                    .transactionAmount(amount).build());

            logger.info("Terminal [{}] payed: [{}]", TERMINAL_ID, ReflectionToStringBuilder.toString(response, ToStringStyle.MULTI_LINE_STYLE, true, true));

    }

    @RequestMapping(value = "/terminal/dayend", method = RequestMethod.POST)
    public void dayEnd() {
        if (!initOk) {
            terminalWasNotInitialized();
        }
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:8080/settlement/dayend/delivery";
        ResponseEntity response = restTemplate.postForObject(fooResourceUrl, transactions, ResponseEntity.class);

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            logger.info("Terminal [{}] Dayend Processing delivered sucessfully.", TERMINAL_ID);
        }
    }

    private void terminalWasNotInitialized() {
        logger.error("The terminal was not initialized.");
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The terminal was not initialized."
        );
    }
}

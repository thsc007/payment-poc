package ch.thsc.payment.poc;

import ch.thsc.payment.poc.bo.Authorization;
import ch.thsc.payment.poc.bo.Terminal;
import ch.thsc.payment.poc.bo.Transaction;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@RestController
@ConditionalOnExpression("${terminal.controller.enabled}")
public class TerminalController {

    private final String TERMINAL_ID = "TID1234";
    private final String MERCHANT = "Merchant 1234";

    @Value("${terminalmanagement.url:http://localhost:8080}")
    private String urlTerminalManagement;
    @Value("${terminalgateway.url:http://localhost:8080}")
    private String urlTerminalGateway;
    @Value("${settlement.url:http://localhost:8080}")
    private String urlSettlementApplication;


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
        String fooResourceUrl = urlTerminalManagement + "/management/init/" + TERMINAL_ID;
        ResponseEntity<Terminal> response = restTemplate.getForEntity(fooResourceUrl, Terminal.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            initOk = true;
            logger.info("TZ [{}] - Terminal [{}] initialized: [{}]", TimeZone.getDefault().getDisplayName(), TERMINAL_ID, ReflectionToStringBuilder.toString(response, ToStringStyle.MULTI_LINE_STYLE, true, true));
        }
    }

    @RequestMapping(value = "/terminal/pay/{card}/{amount}", method = RequestMethod.POST)
    public void pay(@PathVariable String card, @PathVariable Integer amount) {
            checkInit();
            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl = urlTerminalGateway + "/gateway/authorize/" + TERMINAL_ID + "/" + card + "/" + amount;
            ResponseEntity<Authorization> response = restTemplate.getForEntity(fooResourceUrl, Authorization.class);
            transactions.add(Transaction.builder()
                    .merchantName(MERCHANT)
                    .authorizationReferenceNumber(response.getBody().getAuthorizationReferenceNumber())
                    .transactionAmount(amount).build());
            logger.info("TZ [{}] - Terminal [{}] payed: [{}]", TimeZone.getDefault().getDisplayName(), TERMINAL_ID, ReflectionToStringBuilder.toString(response, ToStringStyle.MULTI_LINE_STYLE, true, true));
    }

    @RequestMapping(value = "/terminal/dayend", method = RequestMethod.POST)
    public void dayEnd() {
            checkInit();
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = urlSettlementApplication + "/settlement/dayend/delivery";
        ResponseEntity response = restTemplate.postForObject(fooResourceUrl, transactions, ResponseEntity.class);

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            logger.info("TZ [{}] -Terminal [{}] Dayend Processing delivered sucessfully.", TimeZone.getDefault().getDisplayName(), TERMINAL_ID);
        }
    }

    private void checkInit() {
        if (!initOk) {
            logger.error("TZ [{}] - The terminal was not initialized.", TimeZone.getDefault().getDisplayName());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The terminal was not initialized."
            );
        }
    }
}

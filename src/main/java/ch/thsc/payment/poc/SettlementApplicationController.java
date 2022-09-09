package ch.thsc.payment.poc;

import ch.thsc.payment.poc.bo.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@ConditionalOnExpression("${management.controller.enabled}")
public class SettlementApplicationController {

    private Logger logger = LoggerFactory.getLogger(SettlementApplicationController.class);

    public void processDayEndDelivery(List<Transaction> transactions) {
        Map<String, Set<Transaction>> groupedTrx = transactions.stream().collect(Collectors.groupingBy(t -> t.getMerchantName(), Collectors.toSet()));

        groupedTrx.entrySet().stream().forEach(key -> groupedTrx.get(key).stream().collect(Collectors.summarizingInt(trx -> trx.getTransactionAmount())));

    }
}

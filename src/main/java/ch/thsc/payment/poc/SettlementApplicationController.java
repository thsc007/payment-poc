package ch.thsc.payment.poc;

import ch.thsc.payment.poc.bo.Transaction;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RestController
@ConditionalOnExpression("${settlement.controller.enabled}")
public class SettlementApplicationController {

    private Logger logger = LoggerFactory.getLogger(SettlementApplicationController.class);

    @RequestMapping(value = "/settlement/dayend/delivery", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void processDayEndDelivery(@RequestBody List<Transaction> transactions) {
        if (transactions != null || !transactions.isEmpty()) {
            transactions.stream().forEach(trx ->  logger.info(ReflectionToStringBuilder.toString(trx, ToStringStyle.MULTI_LINE_STYLE, true, true)));

            Map<String, Set<Transaction>> groupedTrx = transactions.stream().collect(Collectors.groupingBy(t -> t.getMerchantName(), Collectors.toSet()));
            groupedTrx.entrySet();


            for (String merchant : groupedTrx.keySet()){
                        logger.info("TZ [{}] - Merchant [{}], sum with statistics [{}]", TimeZone.getDefault().getDisplayName(), merchant
                                , groupedTrx.get(merchant).stream().map(trx -> trx.getTransactionAmount()).collect(Collectors.summarizingInt(Integer::intValue)));
            }

        } else {
            logger.info("TZ [{}] - No transactions where delivered by the day end processing.", TimeZone.getDefault().getDisplayName());
        }
    }
}

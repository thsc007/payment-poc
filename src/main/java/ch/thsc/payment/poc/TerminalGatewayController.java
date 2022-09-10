package ch.thsc.payment.poc;

import ch.thsc.payment.poc.bo.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.TimeZone;

@RestController
@ConditionalOnExpression("${gateway.controller.enabled:true}")
public class TerminalGatewayController {

    private Logger logger = LoggerFactory.getLogger(TerminalGatewayController.class);

    @GetMapping("/gateway/authorize/{terminalId}/{cardNumber}/{amount}")
    public Authorization authorize(@PathVariable String terminalId, @PathVariable String cardNumber, @PathVariable Integer amount) {
        logger.info("TZ [{}] - Check scheme with cardNumber [{}] and amount [{}] - receiving authorization OK.", TimeZone.getDefault().getDisplayName(), cardNumber, amount);
        return new Authorization(cardNumber, amount, "OK");
    }
}

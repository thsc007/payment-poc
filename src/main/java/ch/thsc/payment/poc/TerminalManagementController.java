package ch.thsc.payment.poc;

import ch.thsc.payment.poc.bo.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Collections;

@RestController
@ConditionalOnExpression("${management.controller.enabled}")
public class TerminalManagementController {

    private Logger logger = LoggerFactory.getLogger(TerminalManagementController.class);

    @GetMapping("/management/init/{terminalId}")
    public Terminal getInit(@PathVariable String terminalId) {
        Terminal terminal = new Terminal(terminalId, Collections.singletonList("TWINT"));
        logger.info("Terminal [{}] requested init: [{}]", terminalId, ReflectionToStringBuilder.toString(terminal, ToStringStyle.MULTI_LINE_STYLE, true, true));
        return terminal;
    }
}

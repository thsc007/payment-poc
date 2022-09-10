package ch.thsc.payment.poc;

import ch.thsc.payment.poc.bo.Terminal;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.TimeZone;

@RestController
@ConditionalOnExpression("${management.controller.enabled}")
public class TerminalManagementController {

    private Logger logger = LoggerFactory.getLogger(TerminalManagementController.class);

    @RequestMapping(value = "/management/init/{terminalId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Terminal getInit(@PathVariable String terminalId) {
        Terminal terminal = new Terminal(terminalId, Collections.singletonList("TWINT"));
        logger.info("Z [{}] - Terminal [{}] requested init: [{}]", TimeZone.getDefault().getDisplayName(), terminalId, ReflectionToStringBuilder.toString(terminal, ToStringStyle.MULTI_LINE_STYLE, true, true));
        return terminal;
    }
}

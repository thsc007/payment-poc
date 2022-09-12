package ch.thsc.payment.poc;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String healthCheck() {
        StringBuilder builder = new StringBuilder();
        builder.append("settlement.controller.enabled -> " + System.getProperty("settlement.controller.enabled") + "\n");
        builder.append("terminal.controller.enabled ->" + System.getProperty("terminal.controller.enabled") + "\n");
        builder.append("gateway.controller.enabled ->" + System.getProperty("gateway.controller.enabled") + "\n");
        builder.append("management.controller.enabled ->" + System.getProperty("management.controller.enabled") + "\n");
        return builder.toString();
    }
}

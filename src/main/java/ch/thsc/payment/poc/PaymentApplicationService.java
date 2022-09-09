package ch.thsc.payment.poc;


import org.springframework.stereotype.Component;

@Component
public class PaymentApplicationService {

    public String getInit(String terminalId) {
        return "TID123";
    }
}

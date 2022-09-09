package ch.thsc.payment.poc.bo;

import java.util.UUID;

public class Authorization {

    private String authorizationReferenceNumber = UUID.randomUUID().toString();
    private String cardNumber;
    private Integer authorizationAmount;
    private String status;

    public Authorization(String cardNumber, Integer authorizationAmount, String status){
        this.cardNumber = cardNumber;
        this.authorizationAmount = authorizationAmount;
        this.status = status;
    }
}

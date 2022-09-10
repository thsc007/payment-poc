package ch.thsc.payment.poc.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
//@AllArgsConstructor
public class Authorization {

    private final String authorizationReferenceNumber = UUID.randomUUID().toString();
    private String cardNumber;
    private Integer authorizationAmount;
    private String status;

    public Authorization(String cardNumber, Integer authorizationAmount, String status){
        this.cardNumber = cardNumber;
        this.authorizationAmount = authorizationAmount;
        this.status = status;
    }
}

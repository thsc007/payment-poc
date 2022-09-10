package ch.thsc.payment.poc.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private final String transactionReferenceNumber = UUID.randomUUID().toString();
    private String merchantName;
    private String authorizationReferenceNumber;
    private Integer transactionAmount;

}

package ch.thsc.payment.poc.bo;

import lombok.Getter;

import java.util.UUID;

public class Transaction {


    @Getter
    private final String transactionReferenceNumber = UUID.randomUUID().toString();
    @Getter
    private final String merchantName;
    @Getter
    private final String authorizationReferenceNumber;
    @Getter
    private final Integer transactionAmount;

    public Transaction(String merchantName, String authorizationReferenceNumber, Integer transactionAmount) {
        this.merchantName = merchantName;
        this.authorizationReferenceNumber = authorizationReferenceNumber;
        this.transactionAmount = transactionAmount;
    }
}

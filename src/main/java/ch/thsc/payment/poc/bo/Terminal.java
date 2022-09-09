package ch.thsc.payment.poc.bo;

import java.util.Collections;
import java.util.List;

public class Terminal {

    private final String terminalId;
    private final List<String> brands;

    public Terminal (String terminalId, List<String> brands){
        this.terminalId = terminalId;
        this.brands = brands;
    }
    public String getTerminalId() {
        return terminalId;
    }

    public List<String> getBrands() {
        return Collections.unmodifiableList(brands);
    }
}

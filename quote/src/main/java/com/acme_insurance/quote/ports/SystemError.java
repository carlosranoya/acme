package com.acme_insurance.quote.ports;

import java.util.List;
import java.util.Map;

public class SystemError extends Exception implements ErrorInterface {

    private List<Map<String, Object>> errors;

    public SystemError(List<Map<String, Object>> errors) {
        this.errors = errors;
    }

    @Override
    public List<Map<String, Object>> errors() {
        return errors;
    }

    

}

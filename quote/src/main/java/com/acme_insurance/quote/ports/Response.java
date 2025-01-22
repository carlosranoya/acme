package com.acme_insurance.quote.ports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response<T> implements ErrorInterface {

    private T data;
    private List<Map<String, Object>> errors;
    private Exception ex; 

    public Response(T data, List<Map<String, Object>> errors) {
        this.data = data;
        this.errors = errors;
    }

    public Response(Exception e) {
        ex = e;
        this.errors = Response.getErrorList(e);
    }

    public T getData() {
        return data;
    }

    public void addError(Map<String, Object> errorDescr) {
        errors.add(errorDescr);
    }

    public Exception getException() {
        return ex;
    }

    @Override
    public List<Map<String, Object>> errors() {
        return errors;
    }

    public static List<Map<String, Object>> getErrorList(Exception e) {
        Map<String, Object> item = new HashMap<>();
        item.put("code", 999);
        item.put("message", e.getMessage());
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(item);
        return list;
    }

}

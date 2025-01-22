package com.acme_insurance.quote.ports.common;

import java.util.List;
import java.util.Map;

public interface ErrorInterface {

    List<Map<String, Object>> errors();

}

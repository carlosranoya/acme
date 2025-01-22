package com.acme_insurance.quote.domain.entity;

import java.util.Random;

public class IdGenerator {

    static public Long generate() {

        Long millis = System.currentTimeMillis() % 100000L;
        return new Random(millis).nextLong(100000) * 100000L + millis;

    }

}

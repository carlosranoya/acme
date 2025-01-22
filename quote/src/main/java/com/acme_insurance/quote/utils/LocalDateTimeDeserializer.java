package com.acme_insurance.quote.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 *
 * @author Rodrigo Xavier
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public LocalDateTimeDeserializer() {
        this(null);
    }

    public LocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext dc) {
        try {
            String text = jp.getText();
            return LocalDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}

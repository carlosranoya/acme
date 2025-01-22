package com.acme_insurance.quote.utils;



import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 *
 * @author Rodrigo Xavier
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer() {
        this(null);
    }

    public LocalDateTimeSerializer(Class<LocalDateTime> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            final ZonedDateTime zDate = dateTime.atZone(ZoneOffset.UTC);
            jsonGenerator.writeString(zDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

package com.acme_insurance.quote.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDateSerializer extends StdSerializer<Date> {

    public CustomDateSerializer() {
        this(null);
    }

    public CustomDateSerializer(Class<Date> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(Date dt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            ZonedDateTime zoned = ZonedDateTime.of(
                dt.getYear() + 1900, 
                dt.getMonth() + 1, 
                dt.getDate(), 
                dt.getHours(), 
                dt.getMinutes(), 
                0, 0, 
                ZoneId.of("America/Sao_Paulo"));
            String dateSerialized = zoned.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            jsonGenerator.writeString(dateSerialized);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

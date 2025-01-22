package com.acme_insurance.quote.utils;

import java.util.ArrayList;
import java.util.List;

import com.acme_insurance.quote.domain.value_object.Assistance;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class AssistanceDeserializer extends StdDeserializer<List<Assistance>> {

    public AssistanceDeserializer() {
        this(null);
    }

    public AssistanceDeserializer(Class<List<Assistance>> clazz) {
        super(clazz);
    }

    @Override
    public List<Assistance> deserialize(JsonParser jp, DeserializationContext dc) {
        try {
            
            List<Assistance> list = new ArrayList<>();

            JsonToken token = jp.nextToken();
            while(token != JsonToken.END_ARRAY) {
                list.add(Assistance.fromDescription(jp.getText()));
                token = jp.nextToken();             
            }
            
            return list;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}

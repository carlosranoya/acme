package com.acme_insurance.quote.utils;

import java.util.List;

import com.acme_insurance.quote.domain.value_object.Assistance;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AssistanceSerializer extends StdSerializer<List<Assistance>> {

    public AssistanceSerializer() {
        this(null);
    }

    public AssistanceSerializer(Class<List<Assistance>> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(List<Assistance> assistances, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            int len = assistances.size();
            String[] list = new String[len];
            
            for (int i=0; i < assistances.size(); i++) {
                Assistance a = assistances.get(i);
                list[i] = a.getDescription();
            }
            jsonGenerator.writeArray(list, 0, len);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

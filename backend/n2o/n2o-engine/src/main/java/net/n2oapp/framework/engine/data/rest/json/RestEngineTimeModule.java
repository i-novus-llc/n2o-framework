package net.n2oapp.framework.engine.data.rest.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;

public class RestEngineTimeModule extends SimpleModule {

    public RestEngineTimeModule() {
        addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
        addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
    }

    public RestEngineTimeModule(String[] patterns) {
        this();
        setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (beanDesc.getBeanClass() == String.class) {
                    CustomDateDeserializer dateDeserializer = new CustomDateDeserializer(deserializer);
                    dateDeserializer.setPatterns(patterns);
                    return dateDeserializer;
                }
                return deserializer;
            }
        });
    }

    public RestEngineTimeModule(String[] patterns, String[] exclusionKeys) {
        this();
        setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (beanDesc.getBeanClass() == String.class) {
                    CustomDateDeserializer dateDeserializer = new CustomDateDeserializer(deserializer);
                    dateDeserializer.setPatterns(patterns);
                    dateDeserializer.setExclusions(exclusionKeys);
                    return dateDeserializer;
                }
                return deserializer;
            }
        });
    }
}

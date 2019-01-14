package net.n2oapp.framework.boot.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import net.n2oapp.framework.engine.util.json.BigDecimalSerializer;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Настройка сериализации и десериализации времени и даты
 */
public class N2oJacksonModule extends SimpleModule {

    public N2oJacksonModule(String dateTimePattern) {
        addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(dateTimePattern)));
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimePattern)));
        addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
        addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
        addSerializer(BigDecimal.class, new BigDecimalSerializer());
    }
}


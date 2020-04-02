package net.n2oapp.framework.boot.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import net.n2oapp.framework.engine.util.json.BigDecimalSerializer;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

/**
 * Настройка сериализации и десериализации json для N2O моделей
 */
public class N2oJacksonModule extends SimpleModule {

    public N2oJacksonModule(DateFormat dateFormat) {
        addSerializer(Date.class, new DateSerializer(false, dateFormat));
        addSerializer(BigDecimal.class, new BigDecimalSerializer());
    }
}


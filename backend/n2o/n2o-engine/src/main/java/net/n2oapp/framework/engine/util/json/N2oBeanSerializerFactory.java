package net.n2oapp.framework.engine.util.json;

import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import java.math.BigDecimal;

/**
 * User: operhod
 * Date: 22.11.13
 * Time: 16:54
 */
public class N2oBeanSerializerFactory extends BeanSerializerFactory {
    public N2oBeanSerializerFactory(SerializerFactoryConfig config) {
        super(null);
        _concrete.put(java.sql.Date.class.getName(), DateSerializer.instance);
        _concrete.put(BigDecimal.class.getName(), new BigDecimalSerializer());
    }

    public N2oBeanSerializerFactory() {
        this(null);
    }
}
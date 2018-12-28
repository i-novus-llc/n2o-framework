package net.n2oapp.framework.engine.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author iryabov
 * @since 14.01.2016
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        // put your desired money style here
        jgen.writeString(trim(value.toString()));
    }

    @Override
    public void serializeWithType(BigDecimal value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForScalar(value, gen);
        serialize(value, gen, null);
        typeSer.writeTypeSuffixForScalar(value, gen);
    }

    private String trim(String str) {
        if (!str.contains(".")) return str;
        int index = str.length() - 1;
        while (str.charAt(index) == '0') index--;
        if (str.charAt(index) == '.') index--;
        return str.substring(0, index + 1);
    }

}

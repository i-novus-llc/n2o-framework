package net.n2oapp.framework.api.metadata.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

/**
 * Сериализатор для map, необходим для случая Map<Object, Map<String, String>
 */
public class N2oMapSerializer extends JsonSerializer<Map<String, String>> {
    @Override
    public void serialize(Map<String, String> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeObject(map);
    }
}

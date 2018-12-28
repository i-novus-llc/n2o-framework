package net.n2oapp.framework.api.metadata.global.aware;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author iryabov
 * @since 30.03.2015
 */
public class IdAwareSerializer extends JsonSerializer<IdAware> {

    @Override
    public void serialize(IdAware t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(t.getId());
    }
}

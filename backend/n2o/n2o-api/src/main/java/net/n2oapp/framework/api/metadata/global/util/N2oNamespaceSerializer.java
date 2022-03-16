package net.n2oapp.framework.api.metadata.global.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.n2oapp.framework.api.N2oNamespace;

import java.io.IOException;

/**
 * Cериализация N2oNamespace, необходима для Map<N2oNamespace, Object>
 */
public class N2oNamespaceSerializer extends JsonSerializer<N2oNamespace> {

    @Override
    public void serialize(N2oNamespace n2oNamespace, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        String namespace;
        if (n2oNamespace.getPrefix() == null) {
            namespace = n2oNamespace.getUri();
        } else {
            namespace = n2oNamespace.getPrefix() + "$" + n2oNamespace.getUri();
        }
        jsonGenerator.writeFieldName(namespace);
    }
}

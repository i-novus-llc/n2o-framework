package net.n2oapp.framework.api.metadata.global.util;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import net.n2oapp.framework.api.N2oNamespace;
import org.jdom2.Namespace;

import java.io.IOException;

/**
 * Десериализация N2oNamespace, необходима для Map<N2oNamespace, Object>
 */
public class N2oNamespaceDeserializer extends KeyDeserializer {

    private final String SEPARATOR = "$";

    @Override
    public N2oNamespace deserializeKey(
            String key,
            DeserializationContext ctxt) throws IOException {
        if (key.contains(SEPARATOR))
            return new N2oNamespace(Namespace.getNamespace(key.substring(0, key.indexOf(SEPARATOR)), key.substring(key.indexOf(SEPARATOR) + 1)));
        return new N2oNamespace(Namespace.getNamespace(key));
    }
}

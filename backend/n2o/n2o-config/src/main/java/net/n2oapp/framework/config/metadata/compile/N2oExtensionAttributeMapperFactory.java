package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.engine.factory.EngineNotUniqueException;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.ExtensionAttributeMapper;
import net.n2oapp.framework.api.metadata.compile.ExtensionAttributeMapperFactory;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N2oExtensionAttributeMapperFactory extends BaseMetadataFactory<ExtensionAttributeMapper> implements ExtensionAttributeMapperFactory {

    public N2oExtensionAttributeMapperFactory() {
    }

    public N2oExtensionAttributeMapperFactory(Map<String, ExtensionAttributeMapper> beans) {
        super(beans);
    }

    @Override
    public Map<String, Object> mapAttributes(Map<String, String> attributes, String namespaceUri, CompileProcessor p) {
        List<ExtensionAttributeMapper> mappers = produceList((g, d) -> g.getNamespaceUri().equals(namespaceUri), namespaceUri);
        if (mappers == null || mappers.isEmpty()) {
            return new HashMap<>(attributes);
        }
        if (mappers.size() > 1) {
            throw new EngineNotUniqueException(namespaceUri);
        }
        return mappers.get(0).mapAttributes(attributes, p);
    }
}

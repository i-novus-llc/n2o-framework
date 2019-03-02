package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinder;
import net.n2oapp.framework.api.metadata.compile.MetadataBinderFactory;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import net.n2oapp.framework.config.factory.FactoryPredicates;

import java.util.List;
import java.util.Map;

public class N2oMetadataBinderFactory extends BaseMetadataFactory<MetadataBinder> implements MetadataBinderFactory {

    public N2oMetadataBinderFactory() {
    }

    public N2oMetadataBinderFactory(Map<String, MetadataBinder> beans) {
        super(beans);
    }

    @Override
    public <D extends Compiled> D bind(D compiled, BindProcessor processor) {
        List<MetadataBinder> binders = produceList(FactoryPredicates::isCompiledAssignableFrom, compiled);
        D result = compiled;
        for (MetadataBinder binder : binders) {
            result = (D) binder.bind(result, processor);
        }
        return result;
    }

}

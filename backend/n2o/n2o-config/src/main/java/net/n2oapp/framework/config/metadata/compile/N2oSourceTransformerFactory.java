package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.compile.SourceTransformerFactory;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import net.n2oapp.framework.config.factory.FactoryPredicates;

import java.util.List;
import java.util.Map;

public class N2oSourceTransformerFactory extends BaseMetadataFactory<SourceTransformer<?>> implements SourceTransformerFactory {

    public N2oSourceTransformerFactory() {
    }

    public N2oSourceTransformerFactory(Map<String, SourceTransformer<?>> beans) {
        super(beans);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S> S transform(S source, ValidateProcessor p) {
        List<SourceTransformer<?>> transformers = produceList(FactoryPredicates::isSourceAssignableFrom, source);
        S result = source;
        for (SourceTransformer<?> transformer : transformers) {
            SourceTransformer<S> castedTransformer = (SourceTransformer<S>) transformer;
            if (castedTransformer.matches(result))
                result = castedTransformer.transform(result, p);
        }
        return result;
    }

}

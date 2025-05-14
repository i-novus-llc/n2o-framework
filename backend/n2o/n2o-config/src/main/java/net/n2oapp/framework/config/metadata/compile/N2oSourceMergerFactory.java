package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.compile.SourceMerger;
import net.n2oapp.framework.api.metadata.compile.SourceMergerFactory;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import net.n2oapp.framework.config.factory.FactoryPredicates;
import org.springframework.core.GenericTypeResolver;

import java.util.List;
import java.util.Map;

public class N2oSourceMergerFactory extends BaseMetadataFactory<SourceMerger<?>> implements SourceMergerFactory {

    public N2oSourceMergerFactory() {
    }

    public N2oSourceMergerFactory(Map<String, SourceMerger<?>> beans) {
        super(beans);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S> S merge(S ref, S source) {
        List<SourceMerger<?>> mergers = produceList(FactoryPredicates::isSourceAssignableFrom, ref);
        for (SourceMerger<?> merger : mergers) {
            SourceMerger<S> castedMerger = (SourceMerger<S>) merger;
            Class<?> mergerClass = getMergerClass(castedMerger);
            // skip merger if its parametrized type not compatible with override class
            if (mergerClass != null && mergerClass.isAssignableFrom(source.getClass()))
                source = castedMerger.merge(ref, source);
        }
        return source;
    }

    private Class<?> getMergerClass(SourceMerger<?> merger) {
        return GenericTypeResolver.resolveTypeArgument(merger.getClass(), BaseSourceMerger.class);
    }

}

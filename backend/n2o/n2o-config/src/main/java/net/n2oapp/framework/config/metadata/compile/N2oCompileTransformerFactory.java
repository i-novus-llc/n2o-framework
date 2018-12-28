package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileTransformer;
import net.n2oapp.framework.api.metadata.compile.CompileTransformerFactory;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.factory.FactoryPredicates.isCompiledAssignableFrom;
import static net.n2oapp.framework.config.factory.FactoryPredicates.isOptionalContextAssignableFrom;

public class N2oCompileTransformerFactory extends BaseMetadataFactory<CompileTransformer> implements CompileTransformerFactory {

    public N2oCompileTransformerFactory() {
    }

    public N2oCompileTransformerFactory(Map<String, CompileTransformer> beans) {
        super(beans);
    }

    @Override
    public <D extends Compiled> D transform(D compiled, CompileContext<?, ?> context, CompileProcessor p) {
        List<CompileTransformer> transformers = produceList((g, d) ->
                isCompiledAssignableFrom(g, d) && isOptionalContextAssignableFrom(g, context),
                compiled);
        D result = compiled;
        for (CompileTransformer transformer : transformers) {
            result = (D) transformer.transform(result, context, p);
        }
        return result;
    }

}

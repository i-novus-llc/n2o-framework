package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceCompiler;
import net.n2oapp.framework.api.metadata.compile.SourceCompilerFactory;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;

import java.util.Map;

import static net.n2oapp.framework.config.factory.FactoryPredicates.isSourceEquals;

public class N2oSourceCompilerFactory extends BaseMetadataFactory<SourceCompiler> implements SourceCompilerFactory {

    public N2oSourceCompilerFactory() {
    }

    public N2oSourceCompilerFactory(Map<String, SourceCompiler> beans) {
        super(beans);
    }

    @Override
    public <D extends Compiled, S, C extends CompileContext<?, ?>> D compile(S source, C context, CompileProcessor p) {
        SourceCompiler<D, S, C> compiler = produce((g, s) ->
                isSourceEquals(g, s), source);
        return compiler.compile(source, context, p);
    }

    @Override
    public N2oSourceCompilerFactory add(SourceCompiler... engine) {
        return (N2oSourceCompilerFactory) super.add(engine);
    }
}

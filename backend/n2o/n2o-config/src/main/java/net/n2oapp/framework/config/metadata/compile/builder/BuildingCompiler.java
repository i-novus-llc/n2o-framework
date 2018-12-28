package net.n2oapp.framework.config.metadata.compile.builder;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.building.CompileConstructor;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.CompileBuilder;
import net.n2oapp.framework.api.metadata.compile.SourceCompiler;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

public interface BuildingCompiler<D extends Compiled, S extends Source, C extends CompileContext<?, ?>> extends CompileBuilder<D, S>, SourceCompiler<D, S, C>,
        CompileConstructor<D, S>, SourceClassAware, CompiledClassAware {

    @Override
    default D compile(S source, C context, CompileProcessor p) {
        N2oBuildProcessor<D, S> bp = new N2oBuildProcessor<D, S>(getCompiledClass(), getSourceClass());
        build(bp);
        return bp.build(construct(source), source, context, p);
    }

    @Override
    D construct(S source);

    @Override
    Class<D> getCompiledClass();

    @Override
    Class<S> getSourceClass();
}

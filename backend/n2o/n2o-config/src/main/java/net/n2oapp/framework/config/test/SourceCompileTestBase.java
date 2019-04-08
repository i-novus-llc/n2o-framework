package net.n2oapp.framework.config.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.selective.CompileInfo;

import java.util.stream.Stream;

/**
 * Базовый класс для тестирования сборщиков метаданных {@link net.n2oapp.framework.api.metadata.compile.SourceCompiler}
 */
public abstract class SourceCompileTestBase extends N2oTestBase {

    public ReadTerminalPipeline<ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline>> read(String... uri) {
        if (uri != null)
            Stream.of(uri).forEach(u -> builder.sources(new CompileInfo(u)));
        return builder.read();
    }

    public ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> compile(String... uri) {
        if (uri != null)
            Stream.of(uri).forEach(u -> builder.sources(new CompileInfo(u)));
        return builder.read().compile();
    }

    public ReadCompileBindTerminalPipeline bind(String... uri) {
        if (uri != null)
            Stream.of(uri).forEach(u -> builder.sources(new CompileInfo(u)));
        return builder.read().compile().bind();
    }

    public CompileTerminalPipeline<CompileBindTerminalPipeline> compile() {
        return N2oPipelineSupport.compilePipeline(builder.getEnvironment()).compile();
    }

    public BindTerminalPipeline bind() {
        return N2oPipelineSupport.bindPipeline(builder.getEnvironment()).bind();
    }

    public <D extends Compiled> CompileContext<D, ?> route(String url, Class<D> compiledClass) {
        return builder.route(url, compiledClass);
    }

    public <D extends Compiled> D routeAndGet(String url, Class<D> compiledClass) {
        CompileContext<D, ?> context = builder.route(url, compiledClass);
        return read().compile().bind().get(context, context.getParams(url, null));
    }

    public <D extends Compiled> D routeAndGet(String url, Class<D> compiledClass, DataSet data) {
        CompileContext<D, ?> context = builder.route(url, compiledClass);
        context.getParams(url, null).forEach(data::put);
        return read().compile().bind().get(context, data);
    }
}

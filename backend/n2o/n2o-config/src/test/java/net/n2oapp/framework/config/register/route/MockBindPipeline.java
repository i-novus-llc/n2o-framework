package net.n2oapp.framework.config.register.route;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MockBindPipeline implements ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> {

    private N2oEnvironment environment;
    private Map<String, List<BiConsumer<N2oEnvironment, CompileProcessor>>> mocks = new HashMap<>();

    public MockBindPipeline(N2oEnvironment environment) {
        this.environment = environment;
    }

    public void mock(String sourceId, BiConsumer<N2oEnvironment, CompileProcessor> action) {
        mocks.computeIfAbsent(sourceId, k -> new ArrayList<>());
        mocks.get(sourceId).add(action);
    }

    @Override
    public <D extends Compiled> D get(CompileContext<D, ?> context, CompileProcessor p) {
        List<BiConsumer<N2oEnvironment, CompileProcessor>> actions = mocks.get(context.getSourceId(null));
        if (actions != null)
            actions.forEach(a -> a.accept(environment, p));
        return null;
    }

    @Override
    public ReadCompileBindTerminalPipeline bind() {
        return null;
    }

    @Override
    public ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> transform() {
        return null;
    }

    @Override
    public ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> cache() {
        return null;
    }

    @Override
    public ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> copy() {
        return null;
    }
}

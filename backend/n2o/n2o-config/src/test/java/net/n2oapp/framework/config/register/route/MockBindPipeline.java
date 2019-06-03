package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MockBindPipeline implements ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> {

    private N2oRouteRegister register;
    private Map<String, List<BiConsumer<N2oRouteRegister, Class<?>>>> mocks = new HashMap<>();

    public MockBindPipeline(N2oRouteRegister register) {
        this.register = register;
    }

    public void mock(String sourceId, BiConsumer<N2oRouteRegister, Class<?>> action) {
        mocks.computeIfAbsent(sourceId, k -> new ArrayList<>());
        mocks.get(sourceId).add(action);
    }

    @Override
    public <D extends Compiled> D get(CompileContext<D, ?> context) {
        List<BiConsumer<N2oRouteRegister, Class<?>>> actions = mocks.get(context.getSourceId(null));
        if (actions != null)
            actions.forEach(a -> a.accept(register, context.getCompiledClass()));
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

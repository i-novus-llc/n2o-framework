package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MockBindPipeline implements ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> {

    private N2oRouteRegister register;
    private Map<String, Consumer<N2oRouteRegister>> mocks = new HashMap<>();

    public MockBindPipeline(N2oRouteRegister register) {
        this.register = register;
    }

    public void mock(String sourceId, Consumer<N2oRouteRegister> action) {
        mocks.put(sourceId, action);
    }

    @Override
    public <D extends Compiled> D get(CompileContext<D, ?> context) {
        Consumer<N2oRouteRegister> action = mocks.get(context.getSourceId(null));
        if (action != null)
            action.accept(register);
//        switch (context.getSourceId(null)) {
//            case "p":
//                register.addRoute("/p/w/", new MockCompileContext<>("/p/w", "pW", null, Page.class));
//                break;
//            case "pWc":
//                register.addRoute("/p/w/:id/c/b", new MockCompileContext<>("/p/w/:id/c/b", "pWcB", null, Page.class));
//                break;
//            case "pW1":
//                register.addRoute("/p/w1/:id/c", new MockCompileContext<>("/p/w1/:id/c", "pW1c", null, Page.class));
//                break;
//            case "pW1c":
//                register.addRoute("/p/w1/:id/c/b", new MockCompileContext<>("/p/w1/:id/c/b", "pW1cB", null, Page.class));
//                break;
//            case "pW1cB":
//                register.addRoute("/p/w1/:id/c/b/:name", new MockCompileContext<>("/p/w1/:id/c/b/:name", "pW1cB", null, Page.class));
//                break;
//        }
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

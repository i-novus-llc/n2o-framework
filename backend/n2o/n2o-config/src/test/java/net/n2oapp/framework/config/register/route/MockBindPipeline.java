package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;

public class MockBindPipeline implements ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> {

    private N2oRouteRegister register;

    public MockBindPipeline(N2oRouteRegister register) {
        this.register = register;
    }

    @Override
    public <D extends Compiled> D get(CompileContext<D, ?> context) {
        if (context.getSourceId(null).equals("pWc")) {
            register.addRoute("/p/w/:id/c/b", new MockCompileContext<>("pWcB", null, Page.class));
        } else if (context.getSourceId(null).equals("pW1")) {
            register.addRoute("/p/w1/:id/c", new MockCompileContext<>("pW1c", null, Page.class));
        } else if (context.getSourceId(null).equals("pW1c")) {
            register.addRoute("/p/w1/:id/c/b", new MockCompileContext<>("pW1cB", null, Page.class));
        } else if (context.getSourceId(null).equals("pW1cB")) {
            register.addRoute("/p/w1/:id/c/b/:name", new MockCompileContext<>("pW1cB", null, Page.class));
        }
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

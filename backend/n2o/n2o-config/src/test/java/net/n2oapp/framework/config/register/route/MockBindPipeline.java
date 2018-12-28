package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.metadata.meta.Page;

public class MockBindPipeline implements ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> {

    private N2oRouteRegister register;

    public MockBindPipeline(N2oRouteRegister register) {
        this.register = register;
    }

    @Override
    public <D extends Compiled> D get(CompileContext<D, ?> context) {
        if (context.getSourceId(null).equals("pWc")) {
            register.addRoute(RouteInfoUtil.createRouteInfo("/p/w/:id/c/b", "pWcB", Page.class));
        } else if (context.getSourceId(null).equals("pW1")) {
            register.addRoute(RouteInfoUtil.createRouteInfo("/p/w1/:id/c", "pW1c",  Page.class));
        } else if (context.getSourceId(null).equals("pW1c")) {
            register.addRoute(RouteInfoUtil.createRouteInfo("/p/w1/:id/c/b", "pW1cB",  Page.class));
        } else if (context.getSourceId(null).equals("pW1cB")) {
            register.addRoute(RouteInfoUtil.createRouteInfo("/p/w1/:id/c/b/:name", "pW1cB", Page.class));
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

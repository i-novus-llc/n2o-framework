package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.config.metadata.compile.context.BaseCompileContext;

public class MockCompileContext<D extends Compiled, S> extends BaseCompileContext<D,S> {

    public MockCompileContext(String route, String sourceId, Class<S> sourceClass, Class<D> compiledClass) {
        super(route, sourceId, sourceClass, compiledClass);
    }
}

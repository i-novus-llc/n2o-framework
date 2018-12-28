package net.n2oapp.framework.api.test;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.user.StaticUserContext;

/**
 * @author iryabov
 * @since 25.07.2016
 */
public class TestStaticUserContext extends StaticUserContext {

    public TestStaticUserContext(ContextEngine context) {
        super(context);
    }

    @Override
    public void setContext(ContextEngine contextEngine) {
        StaticUserContext.context = contextEngine;
    }
}

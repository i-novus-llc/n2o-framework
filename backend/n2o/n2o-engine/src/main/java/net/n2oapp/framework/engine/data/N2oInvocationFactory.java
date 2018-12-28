package net.n2oapp.framework.engine.data;

import net.n2oapp.engine.factory.integration.spring.SpringEngineFactory;
import net.n2oapp.framework.api.data.ActionInvocationEngine;

/**
 * Фабрика сервисов вызова действий
 */
public class N2oInvocationFactory extends SpringEngineFactory<Class, ActionInvocationEngine> {

    @Override
    public Class<ActionInvocationEngine> getEngineClass() {
        return ActionInvocationEngine.class;
    }

    @Override
    public Class getType(ActionInvocationEngine engine) {
        return (Class) engine.getType();
    }
}

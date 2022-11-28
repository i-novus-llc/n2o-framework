package net.n2oapp.framework.api.metadata.meta.action;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Знание о классе с действием клиента
 */
public interface ActionAware extends Compiled {
    Action getAction();
    void setAction(Action action);
}

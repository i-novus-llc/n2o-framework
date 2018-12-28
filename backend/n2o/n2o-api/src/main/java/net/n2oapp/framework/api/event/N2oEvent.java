package net.n2oapp.framework.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * Событие жизненного цикла N2O
 */
public abstract class N2oEvent extends ApplicationEvent {
    public N2oEvent(Object source) {
        super(source);
    }
}

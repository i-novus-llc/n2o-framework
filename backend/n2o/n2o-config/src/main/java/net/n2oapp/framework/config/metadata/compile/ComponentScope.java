package net.n2oapp.framework.config.metadata.compile;

import java.io.Serializable;

/**
 * Компонент-обёртка.
 * Применяется для передачи родительского компонента в сборку к дочернему
 */
public class ComponentScope implements Serializable {
    private Object component;

    public ComponentScope(Object component) {
        this.component = component;
    }

    public <T> T unwrap(Class<T> clazz) {
        if (clazz.isAssignableFrom(component.getClass()))
            return (T) component;
        else
            return null;
    }

}

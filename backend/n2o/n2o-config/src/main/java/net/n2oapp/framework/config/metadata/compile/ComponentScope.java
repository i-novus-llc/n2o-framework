package net.n2oapp.framework.config.metadata.compile;

import java.util.function.Function;

/**
 * Компонент-обёртка.
 * Применяется для передачи родительского компонента в сборку к дочернему
 */
public class ComponentScope {
    private Object component;
    private ComponentScope parentScope;

    public ComponentScope(Object component) {
        this.component = component;
    }

    public ComponentScope(Object component, ComponentScope parentScope) {
        this.component = component;
        this.parentScope = parentScope;
    }

    public <T> T unwrap(Class<T> clazz) {
        if (clazz.isAssignableFrom(component.getClass()))
            return (T) component;
        else
            return null;
    }

    public <T, R> R getFirstNotNull(Class<T> clazz, Function<T,R> function) {
        return getFirstNotNull(this, clazz, function);
    }

    public static <T, R> R getFirstNotNull(ComponentScope componentScope, Class<T> clazz, Function<T,R> function) {
        while (componentScope != null) {
            T unwrapped = componentScope.unwrap(clazz);
            if (unwrapped != null && function.apply(unwrapped) != null) {
                return function.apply(unwrapped);
            }
            componentScope = componentScope.getParentScope();
        }

        return null;
    }

    public ComponentScope getParentScope() {
        return parentScope;
    }
}

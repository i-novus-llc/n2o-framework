package net.n2oapp.framework.config.metadata.compile;

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

    public ComponentScope getParentScope() {
        return parentScope;
    }
}

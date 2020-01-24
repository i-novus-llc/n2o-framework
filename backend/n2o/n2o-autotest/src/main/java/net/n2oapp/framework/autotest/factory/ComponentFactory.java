package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.component.Component;
import net.n2oapp.framework.autotest.component.ComponentLibrary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ComponentFactory {
    private Set<Object> register = new HashSet<>();

    public <T extends Component> T component(SelenideElement element,
                                             Class<? extends T> componentClass) {
        T component = findOrProduce(componentClass);
        component.setElement(element);
        if (component instanceof FactoryAware)
            ((FactoryAware)component).setFactory(this);
        return component;
    }

    public void components(Object... components) {
        register.addAll(Arrays.asList(components));
    }

    public void library(ComponentLibrary library) {
        register.addAll(library.components());
    }

    @SuppressWarnings("unchecked")
    private <T> T findOrProduce(Class<? super T> componentClass) {
        Optional<T> found = find(componentClass);
        if (!found.isPresent() && componentClass.isInterface())
            throw new IllegalArgumentException("Component not found for " + componentClass);
        if (found.isPresent())
            return found.get();
        try {
            return (T) componentClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> Optional<T> find(Class<? super T> componentClass) {
        Optional<T> found = Optional.empty();
        for (int level = 0; level < 3; level++) {
            found = findByLevel(componentClass, level);
            if (found.isPresent())
                break;
        }
        return found;
    }


    @SuppressWarnings("unchecked")
    private <T> Optional<T> findByLevel(Class<? super T> componentClass, int level) {
        for (Object component : register) {
            if (matchComponent(componentClass, component, level).isPresent())
                return (Optional<T>) Optional.of(component);
        }
        return Optional.empty();
    }

    private Optional<Object> matchComponent(Class<?> componentClass, Object component, int level) {
        switch (level) {
            case 0:
                if (!componentClass.isInterface() && componentClass.equals(component.getClass()))
                    return Optional.of(component);
                break;
            case 1:
                if (!componentClass.isInterface() && componentClass.equals(component.getClass().getSuperclass()))
                    return Optional.of(component);
                break;
            case 2:
                if (componentClass.isInterface() && component.getClass().getInterfaces().length > 0) {
                    for (Class<?> anInterface : component.getClass().getInterfaces()) {
                        if (anInterface.equals(componentClass))
                            return Optional.of(component);
                    }
                }
                break;
            default:
                if (componentClass.isAssignableFrom(component.getClass()))
                    return Optional.of(component);
        }
        return Optional.empty();
    }
}

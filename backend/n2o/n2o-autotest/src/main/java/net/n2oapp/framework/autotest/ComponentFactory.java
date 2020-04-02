package net.n2oapp.framework.autotest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.ComponentLibrary;
import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Фабрика реализаций компонентов для автотестирования
 */
public class ComponentFactory {
    private Set<Class<?>> components = new HashSet<>();
    private Set<Class<?>> collections = new HashSet<>();

    @SuppressWarnings("unchecked")
    public <T extends Component> T produce(SelenideElement element,
                                           Class<T> componentClass) {
        T component = (T) findAndProduce(componentClass, components);
        component.setElement(element);
        return component;
    }

    @SuppressWarnings("unchecked")
    public <T extends ComponentsCollection> T produce(ElementsCollection elements,
                                                      Class<T> collectionClass) {
        T collection = (T) findAndProduce(collectionClass, collections);
        collection.setElements(elements);
        return collection;
    }

    @SafeVarargs
    public final ComponentFactory addComponents(Class<? extends Component>... components) {
        this.components.addAll(Arrays.asList(components));
        return this;
    }

    @SafeVarargs
    public final ComponentFactory addCollections(Class<? extends ComponentsCollection>... collections) {
        this.collections.addAll(Arrays.asList(collections));
        return this;
    }

    public ComponentFactory addLibrary(ComponentLibrary library) {
        components.addAll(library.components());
        collections.addAll(library.collections());
        return this;
    }

    private Object findAndProduce(Class<?> targetClass, Set<Class<?>> candidates) {
        Optional<Class<?>> found = find(targetClass, candidates);
        if (!found.isPresent())
            throw new IllegalArgumentException("Implementation not found for " + targetClass);
        try {
            return found.get().getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Optional<Class<?>> find(Class<?> targetClass, Set<Class<?>> candidates) {
        if (targetClass.isInterface()) {
            for (Class<?> candidate : candidates) {
                if (targetClass.isAssignableFrom(candidate))
                    return Optional.of(candidate);
            }
        } else {
            return Optional.of(targetClass);
        }
        return Optional.empty();
    }
}

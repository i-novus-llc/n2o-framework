package net.n2oapp.framework.autotest.api;

import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.Component;

import java.util.Collections;
import java.util.List;

public interface ComponentLibrary {
    default List<Class<? extends Component>> components() {
        return Collections.emptyList();
    }
    default List<Class<? extends ComponentsCollection>> collections() {
        return Collections.emptyList();
    }
}

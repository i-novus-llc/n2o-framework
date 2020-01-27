package net.n2oapp.framework.autotest.impl;

import net.n2oapp.framework.autotest.api.ComponentLibrary;
import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.impl.collection.*;
import net.n2oapp.framework.autotest.impl.component.page.N2oLeftRightPage;

import java.util.Arrays;
import java.util.List;

/**
 * Библиотека стандартных компонентов N2O для автотестирования
 */
public class N2oComponentLibrary implements ComponentLibrary {
    @Override
    public List<Class<? extends Component>> components() {
        return Arrays.asList(N2oLeftRightPage.class);
    }

    @Override
    public List<Class<? extends ComponentsCollection>> collections() {
        return Arrays.asList(N2oCells.class, N2oControls.class, N2oFields.class, N2oRegions.class, N2oTableHeaders.class,
                N2oToolbar.class, N2oWidgets.class);
    }
}

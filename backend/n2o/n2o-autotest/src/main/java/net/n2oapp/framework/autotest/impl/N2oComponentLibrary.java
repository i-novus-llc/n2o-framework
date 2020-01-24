package net.n2oapp.framework.autotest.impl;

import net.n2oapp.framework.autotest.component.Component;
import net.n2oapp.framework.autotest.component.ComponentLibrary;
import net.n2oapp.framework.autotest.impl.page.N2oLeftRightPage;

import java.util.Arrays;
import java.util.List;

public class N2oComponentLibrary implements ComponentLibrary {
    @Override
    public List<? extends Component> components() {
        return Arrays.asList(new N2oLeftRightPage(), new N2oLeftRightPage());
    }
}

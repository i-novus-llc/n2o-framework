package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.cell.Cell;
import net.n2oapp.framework.autotest.component.cell.TextCell;
import net.n2oapp.framework.autotest.impl.N2oComponentsCollection;

public class Cells extends N2oComponentsCollection {
    public Cells(ElementsCollection elements, ComponentFactory factory) {
        super(elements, factory);
    }

    public TextCell cell(int index) {
        return null;
    }

    public <T extends Cell> T cell(int index, Class<T> componentClass) {
        return null;
    }
}

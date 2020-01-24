package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.header.StandardTableHeader;
import net.n2oapp.framework.autotest.component.header.TableHeader;
import net.n2oapp.framework.autotest.impl.N2oComponentsCollection;

public class TableHeaders extends N2oComponentsCollection {

    public TableHeaders(ElementsCollection elements, ComponentFactory factory) {
        super(elements, factory);
    }

    public StandardTableHeader header(int index) {
        return null;
    }

    public StandardTableHeader header(Condition by) {
        return null;
    }

    public <T extends TableHeader> T header(int index, Class<T> componentClass) {
        return null;
    }

    public <T extends TableHeader> T header(Condition by, Class<T> componentClass) {
        return null;
    }
}

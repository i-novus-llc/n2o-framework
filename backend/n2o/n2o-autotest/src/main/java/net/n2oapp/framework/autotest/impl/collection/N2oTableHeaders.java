package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.header.StandardTableHeader;
import net.n2oapp.framework.autotest.api.component.header.TableHeader;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oTableHeaders extends N2oComponentsCollection implements TableHeaders {

    public StandardTableHeader header(int index) {
        return header(index, StandardTableHeader.class);
    }

    public StandardTableHeader header(Condition findBy) {
        return header(findBy, StandardTableHeader.class);
    }

    public <T extends TableHeader> T header(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    public <T extends TableHeader> T header(Condition findBy, Class<T> componentClass) {
        return component(elements().findBy(findBy), componentClass);
    }
}

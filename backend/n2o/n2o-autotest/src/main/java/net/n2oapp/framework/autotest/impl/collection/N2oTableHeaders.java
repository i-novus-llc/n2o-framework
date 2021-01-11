package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableHeader;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Заголовки столбцов таблицы для автотестирования
 */
public class N2oTableHeaders extends N2oComponentsCollection implements TableHeaders {

    public TableSimpleHeader header(int index) {
        return header(index, TableSimpleHeader.class);
    }

    public TableSimpleHeader header(Condition findBy) {
        return header(findBy, TableSimpleHeader.class);
    }

    public <T extends TableHeader> T header(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    public <T extends TableHeader> T header(Condition findBy, Class<T> componentClass) {
        return component(elements().findBy(findBy), componentClass);
    }
}

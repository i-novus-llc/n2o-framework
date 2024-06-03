package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.widget.table.TableHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Заголовки столбцов таблицы для автотестирования
 */
public class N2oTableHeaders extends N2oComponentsCollection implements TableHeaders {

    public TableSimpleHeader header(int index) {
        return header(index, TableSimpleHeader.class);
    }

    public TableSimpleHeader header(WebElementCondition findBy) {
        return header(findBy, TableSimpleHeader.class);
    }

    public <T extends TableHeader> T header(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    public <T extends TableHeader> T header(WebElementCondition findBy, Class<T> componentClass) {
        return component(elements().findBy(findBy), componentClass);
    }
}

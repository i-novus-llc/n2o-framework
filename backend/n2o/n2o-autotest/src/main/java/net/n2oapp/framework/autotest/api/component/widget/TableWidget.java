package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.impl.collection.N2oFields;
import net.n2oapp.framework.autotest.impl.collection.N2oTableHeaders;

/**
 * Виджет - таблица для автотестирования
 */
public interface TableWidget extends StandardWidget {
    Columns columns();
    Filters filters();
    Paging paging();


    void shouldBeFiltersExpanded();
    void shouldBeFiltersCollapsed();

    interface Columns {
        N2oTableHeaders headers();
        Rows rows();
    }

    interface Filters {
        N2oFields fields();
    }

    interface Paging {

    }

    interface Rows {
        Cells row(int index);
        void shouldHaveSize(int size);
        void shouldNotHaveRows();
    }
}

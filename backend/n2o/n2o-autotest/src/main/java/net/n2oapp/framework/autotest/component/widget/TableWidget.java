package net.n2oapp.framework.autotest.component.widget;

import net.n2oapp.framework.autotest.factory.Cells;
import net.n2oapp.framework.autotest.factory.Fields;
import net.n2oapp.framework.autotest.factory.TableHeaders;

public interface TableWidget extends StandardWidget {
    Columns columns();
    Filters filters();
    Paging paging();


    void shouldBeFiltersExpanded();
    void shouldBeFiltersCollapsed();

    interface Columns {
        TableHeaders headers();
        Rows rows();
    }

    interface Filters {
        Fields fields();
    }

    interface Paging {

    }

    interface Rows {
        Cells row(int index);
        void shouldHaveSize(int size);
        void shouldNotHaveRows();
    }
}

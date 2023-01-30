package net.n2oapp.framework.autotest.api.component.widget.table;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.SortingDirection;
import net.n2oapp.framework.autotest.api.collection.*;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

import java.util.List;

/**
 * Виджет таблица для автотестирования
 */
public interface TableWidget extends StandardWidget {
    Columns columns();

    Filters filters();

    Paging paging();

    interface Columns {
        TableHeaders headers();

        Rows rows();
    }

    interface Filters {

        Toolbar toolbar();

        Fields fields();

        FieldSets fieldsets();

        void search();

        void clear();

        void shouldBeVisible();

        void shouldBeHidden();

        @Deprecated
        void shouldBeInvisible();
    }

    interface Rows {
        Cells row(int index);

        void shouldHaveSize(int size);

        void shouldNotHaveRows();

        void shouldBeSelected(int row);

        void shouldNotHaveSelectedRows();

        void columnShouldHaveTexts(int index, List<String> text);
        List<String> columnTexts(int index);

        void columnShouldBeSortedBy(int columnIndex, SortingDirection direction);
    }
}

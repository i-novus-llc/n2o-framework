package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;

import java.util.List;

/**
 * Виджет - таблица для автотестирования
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
        Fields fields();

        void search();

        void clear();

        void shouldBeVisible();

        void shouldBeInvisible();
    }

    interface Paging {
        void activePageShouldBe(String number);

        void selectPage(String number);

        void pagingShouldHave(String number);

        int totalElements();

        void totalElementsShouldBe(int count);
    }

    interface Rows {
        Cells row(int index);

        void shouldHaveSize(int size);

        void shouldNotHaveRows();

        void shouldBeSelected(int row);

        void columnShouldHaveTexts(int index, List<String> text);

        List<String> columnTexts(int index);
    }
}

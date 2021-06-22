package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oPaging;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

import java.util.List;

/**
 * Виджет таблица для автотестирования
 */
public class N2oTableWidget extends N2oStandardWidget implements TableWidget {
    @Override
    public Columns columns() {
        return new N2oColumns();
    }

    @Override
    public Filters filters() {
        return new N2oFilters();
    }

    @Override
    public Paging paging() {
        return new N2oPaging(element());
    }

    public class N2oFilters implements Filters {

        @Override
        public Toolbar toolbar() {
            return N2oSelenide.collection(element().$$(".n2o-filter .btn"), Toolbar.class);
        }

        @Override
        public Fields fields() {
            return N2oSelenide.collection(element().$$(".n2o-filter .n2o-fieldset .n2o-form-group"), Fields.class);
        }

        @Override
        public void search() {
            element().$$(".n2o-filter .btn-group .btn").findBy(Condition.text("Найти")).click();
        }

        @Override
        public void clear() {
            element().$$(".n2o-filter .btn-group .btn").findBy(Condition.text("Сбросить")).click();
        }

        @Override
        public void shouldBeVisible() {
            element().$(".n2o-filter").shouldBe(Condition.visible);
        }

        @Override
        public void shouldBeHidden() {
            element().$(".n2o-filter").shouldBe(Condition.hidden);
        }

        @Deprecated
        public void shouldBeInvisible() {
            shouldBeHidden();
        }
    }

    public class N2oColumns implements Columns {

        @Override
        public TableHeaders headers() {
            return N2oSelenide.collection(element().$$(".n2o-advanced-table-thead th.n2o-advanced-table-header-cel"), TableHeaders.class);
        }

        @Override
        public Rows rows() {
            return new N2oRows();
        }

    }

    public class N2oRows implements Rows {

        @Override
        public Cells row(int index) {
            return N2oSelenide.collection(element().$$(".n2o-advanced-table-tbody tr:nth-child(" + (++index) + ") td"), Cells.class);
        }

        @Override
        public void shouldHaveSize(int size) {
            element().$$(".n2o-advanced-table-tbody .n2o-table-row").shouldHaveSize(size);
        }

        @Override
        public void shouldNotHaveRows() {
            element().$$(".n2o-advanced-table-tbody .n2o-table-row").shouldBe(CollectionCondition.empty);
        }

        @Override
        public void shouldBeSelected(int row) {
            element().$$(".n2o-advanced-table-tbody .n2o-table-row").get(row).shouldHave(Condition.cssClass("table-active"));
        }

        @Override
        public void shouldNotHaveSelectedRows() {
            element().$$(".n2o-table-row.table-active").shouldHaveSize(0);
        }

        @Override
        public void columnShouldHaveTexts(int index, List<String> texts) {
            if (texts == null || texts.isEmpty())
                element().$$(".n2o-table-row td:nth-child(" + (++index) + ")").shouldHave(CollectionCondition.empty);
            else
                element().$$(".n2o-table-row td:nth-child(" + (++index) + ")").shouldHave(CollectionCondition.texts(texts));
        }

        @Override
        public List<String> columnTexts(int index) {
            return element().should(Condition.exist).$$(".n2o-table-row td:nth-child(" + (++index) + ")").texts();
        }
    }
}

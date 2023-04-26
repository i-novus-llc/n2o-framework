package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.SortingDirection;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.*;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oPaging;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public void shouldBeDisabled() {
        element().$(".n2o-standard-widget-layout").shouldHave(Condition.cssClass("n2o-disabled"));
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
        public FieldSets fieldsets() {
            return N2oSelenide.collection(element().$$(".n2o-fieldset"), FieldSets.class);
        }

        @Override
        public void shouldBeVisible() {
            filter().shouldBe(Condition.visible);
        }

        @Override
        public void shouldBeHidden() {
            filter().shouldBe(Condition.hidden);
        }

        @Deprecated
        public void shouldBeInvisible() {
            shouldBeHidden();
        }

        protected SelenideElement filter() {
            return element().$(".n2o-filter");
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
            return N2oSelenide.collection(rows().get(index).$$("td"), Cells.class);
        }

        @Override
        public void shouldHaveSize(int size) {
            rows().shouldHave(CollectionCondition.size(size));
        }

        @Override
        public void shouldNotHaveRows() {
            rows().shouldBe(CollectionCondition.empty);
        }

        @Override
        public void shouldBeSelected(int row) {
            rows().get(row).shouldHave(Condition.cssClass("table-active"));
        }

        @Override
        public void shouldNotHaveSelectedRows() {
            rows().filterBy(Condition.cssClass("table-active"))
                    .shouldHave(CollectionCondition.size(0));
        }

        @Override
        public void columnShouldHaveTexts(int index, List<String> texts) {
            column(index).shouldHave(CollectionCondition.texts(texts));
        }

        @Override
        public void columnShouldBeEmpty(int index) {
            column(index).shouldHave(CollectionCondition.empty);
        }

        @Override
        public List<String> columnTexts(int index) {
            return column(index).texts();
        }

        @Override
        public void columnShouldBeSortedBy(int columnIndex, SortingDirection direction) {
            ElementsCollection elements = element().should(Condition.exist).$$(".n2o-table-row td:nth-child(" + (++columnIndex) + ")");

            switch (direction) {
                case ASC:
                    elements.should(CollectionCondition.exactTexts(
                            elements.texts().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList())
                    ));
                    break;
                case DESC:
                    elements.should(CollectionCondition.exactTexts(
                            elements.texts().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList())
                    ));
                    break;
            }
        }

        protected ElementsCollection column(int index) {
            return element().$$(String.format(".n2o-table-row td:nth-child(%d)", ++index));
        }

        protected ElementsCollection rows() {
            return element().$$(".n2o-advanced-table-tbody .n2o-table-row");
        }
    }
}

package net.n2oapp.framework.autotest.impl.component.widget.cards;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.widget.cards.Card;
import net.n2oapp.framework.autotest.impl.collection.N2oComponentsCollection;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Компонент Card для автотестирования
 */
public class N2oCard extends N2oComponent implements Card {

    @Override
    public Columns columns() {
        N2oColumns columns = new N2oColumns();
        columns.setElements(element().$$(".n2o-cards__item"));
        return columns;
    }

    public class N2oColumns extends N2oComponentsCollection implements Columns {
        @Override
        public Column column(int index) {
            N2oColumn column = new N2oColumn();
            column.setElement(elements().get(index));
            return column;
        }
    }

    public class N2oColumn extends N2oComponent implements Column {
        @Override
        public Cells blocks() {
            return N2oSelenide.collection(element().$$(".n2o-cards__item > span, .n2o-cards__item > div"), Cells.class);
        }

    }
}

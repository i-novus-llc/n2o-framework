package net.n2oapp.framework.autotest.impl.component.widget.cards;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.widget.cards.Card;
import net.n2oapp.framework.autotest.impl.collection.N2oComponentsCollection;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

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
        public Blocks blocks() {
            N2oBlocks blocks = new N2oBlocks();
            blocks.setElements(element().$$(".n2o-cards__item > span, .n2o-cards__item > div"));
            return blocks;
        }

        @Override
        public void shouldHaveWidth(int size) {
            element().shouldHave(Condition.cssClass("col-" + size));
        }
    }

    public class N2oBlocks extends N2oComponentsCollection implements Blocks {
        @Override
        public Block block(int index) {
            N2oBlock block = new N2oBlock();
            block.setElement(elements().get(index));
            return block;
        }
    }

    public class N2oBlock extends N2oComponent implements Block {
        @Override
        public <T extends Cell> T cell(Class<T> componentClass) {
            return component(element(), componentClass);
        }

        @Override
        public void shouldHaveStyle(String style) {
            element().shouldHave(Condition.attributeMatching("style", ".*" + style + ".*"));
        }

        @Override
        public void shouldHaveCssClass(String cssClass) {
            element().shouldHave(Condition.cssClass(cssClass));
        }
    }
}

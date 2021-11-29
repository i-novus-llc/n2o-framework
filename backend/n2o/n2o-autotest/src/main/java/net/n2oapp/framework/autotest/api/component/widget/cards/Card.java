package net.n2oapp.framework.autotest.api.component.widget.cards;

import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.cell.Cell;

/**
 * Компонент Card для автотестирования
 */
public interface Card extends Component {

    Columns columns();

    interface Columns extends ComponentsCollection {
        Column column(int index);
    }

    interface Column extends Component {
        Blocks blocks();

        void shouldHaveWidth(int size);
    }

    interface Blocks extends ComponentsCollection {
        Block block(int index);
    }

    interface Block extends Component {
        <T extends Cell> T cell(Class<T> componentClass);

        void shouldHaveStyle(String style);

        void shouldHaveCssClass(String cssClass);
    }
}

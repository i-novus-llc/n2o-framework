package net.n2oapp.framework.autotest.api.component.widget.cards;

import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент Card для автотестирования
 */
public interface Card extends Component {

    Columns columns();

    interface Columns extends ComponentsCollection {
        Column column(int index);
    }

    interface Column extends Component {
        Cells blocks();
    }
}

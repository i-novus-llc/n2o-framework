package net.n2oapp.framework.autotest.api.component.widget.tiles;

import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент панель для автотестирования
 */
public interface Tile extends Component {
    Cells blocks();
}

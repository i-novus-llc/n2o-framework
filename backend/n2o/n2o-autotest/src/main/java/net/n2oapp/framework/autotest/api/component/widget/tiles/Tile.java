package net.n2oapp.framework.autotest.api.component.widget.tiles;

import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент панель для автотестирования
 */
public interface Tile extends Component {

    /**
     * Возвращает блоки с содержанием в виде ячеек
     * @return Ячейки таблицы для автотестирования
     */
    Cells blocks();

    /**
     * Проверка ширины панели
     * @param width ожидаемая ширина
     */
    void shouldHaveWidth(int width);

    /**
     * Проверка высоты панели
     * @param height ожидаемая высота
     */
    void shouldHaveHeight(int height);
}

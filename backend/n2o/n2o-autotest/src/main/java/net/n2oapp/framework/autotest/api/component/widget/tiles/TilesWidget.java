package net.n2oapp.framework.autotest.api.component.widget.tiles;

import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

/**
 * Виджет панели для автотестирования
 */
public interface TilesWidget extends StandardWidget {
    /**
     * Возвращает компонент панель необходимого номер
     * @param index номер панели
     * @return Компонент панель для автотестирования
     */
    Tile tile(int index);

    /**
     * Проверка количества панелей в виджете
     * @param count ожидаемое количество
     */
    void shouldHaveItems(int count);

    /**
     * @return Компонент пагинации для автотестирования
     */
    Paging paging();
}

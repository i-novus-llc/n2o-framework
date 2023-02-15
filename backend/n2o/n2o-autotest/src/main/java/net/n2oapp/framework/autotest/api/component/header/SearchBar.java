package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.collection.SearchResult;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Панель поиска в шапке для автотестирования
 */
public interface SearchBar extends Component {

    /**
     * Клик по панели
     */
    void click();

    /**
     * Ввод и поиск значения
     * @param title вводимое значение
     */
    void search(String title);

    /**
     * Проверка значения внутри поля поиска на соответствие
     * @param value ожидаемое значение
     */
    void shouldHaveValue(String value);

    /**
     * @return Список с результатами поиска в шапке для автотестирования
     */
    SearchResult searchResult();

    /**
     * Очистка поля поиска
     */
    void clear();

}
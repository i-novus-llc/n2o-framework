package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Элемент выпадающего списка поиска в шапке для автотестирования
 */
public interface SearchItem extends Component {

    /**
     * Проверка заголовка на соответствие
     * @param title ожидаемый заголовок
     */
    void shouldHaveTitle(String title, Duration... duration);

    /**
     * Проверка ссылки на соответствие
     * @param url ожидаемое значение ссылки
     */
    void shouldHaveLink(String url);

    /**
     * Проверка описания на соответствие
     * @param description ожидаемое описание
     */
    void shouldHaveDescription(String description);

    /**
     * Проверка иконки на соответствие
     * @param icon ожидаемое описание
     */
    void shouldHaveIcon(String icon);

}
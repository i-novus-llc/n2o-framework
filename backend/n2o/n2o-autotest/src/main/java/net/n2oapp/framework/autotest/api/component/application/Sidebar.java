package net.n2oapp.framework.autotest.api.component.application;

import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент боковой панели для автотестирования
 */
public interface Sidebar extends Component {

    /**
     * Проверка точного соответствия текста заголовка (без учета регистра) ожидаемому значению
     * @param title ожидаемый текст заголовка
     */
    void shouldHaveTitle(String title);

    /**
     * Проверка пути к файлу с логотипом на соответствие ожидаемому значению
     * @param regex регулярное выражение в соответствии с ожидаемым путем к логотипу
     */
    void shouldHaveBrandLogoMatches(String regex);

    /**
     * Проверка точного соответствия текста подзаголовка ожидаемому значению
     * @param subtitle ожидаемое значение заголовка
     */
    void shouldHaveSubtitle(String subtitle);

    /**
     * Проверка зафиксированности сайдбара
     */
    void shouldBeFixed();

    /**
     * Проверка того, что сайдбар находится на правой стороне страницы
     */
    void shouldBeRight();

    /**
     * Проверка того, что сайдбар перекрывает страницу
     */
    void shouldBeOverlay();

    /**
     * Проверяет состояние скрытости сайдбара по умолчанию
     * @param state ожидаемое состояние
     */
    void shouldHaveState(SidebarState state);


    /**
     * Клик по кнопке раскрытия сайдбара
     */
    void clickToggleBtn();

    /**
     * Возвращает меню с элементами из nav
     * @return Компонент меню для автотестирования
     */
    Menu nav();

    /**
     * Возвращает меню с элементами из extra-menu
     * @return Компонент меню для автотестирования
     */
    Menu extra();

}

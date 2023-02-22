package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент header для автотестирования
 */
public interface SimpleHeader extends Component {

    /**
     * Проверка текста у бренда
     * @param brandName ожидаемый текст
     */
    void shouldHaveBrandName(String brandName);

    /**
     * @return Меню для автотестирования
     */
    Menu nav();

    /**
     * @return Меню для автотестирования
     */
    Menu extra();

    /**
     * @return Панель поиска в шапке для автотестирования
     */
    SearchBar search();

    /**
     * Проверка наличия переключателя(иконки) боковой панели
     */
    void shouldHaveSidebarSwitcher();

    /**
     * Клик по иконки переключателя боковой панели
     */
    void switchSidebar();

}

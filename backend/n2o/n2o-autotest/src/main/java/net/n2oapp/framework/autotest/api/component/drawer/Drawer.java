package net.n2oapp.framework.autotest.api.component.drawer;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.Page;

/**
 * Окно drawer для автотестирования
 */
public interface Drawer extends Component {

    /**
     * Возвращает страницу контента, лежащего внутри компонента drawer
     * @param pageClass тип возвращаемой страницы
     * @return компонент страница для автотестирования
     */
    <T extends Page> T content(Class<T> pageClass);

    /**
     * @return компонент панель кнопок для автотестирования
     */
    DrawerToolbar toolbar();

    /**
     * Проверяет соответствие заголовка окна
     * @param text ожидаемый текст заголовка
     */
    void shouldHaveTitle(String text);

    /**
     * Проверка соответствия положения окна
     * @param placement ожидаемое положение окна
     */
    void shouldHavePlacement(Placement placement);

    /**
     * Проверка соответствия ширины выезжаемого окна
     * @param width ожидаемая ширина окна
     */
    void shouldHaveWidth(String width);

    /**
     * Проверка соответствия высоты выезжаемого окна
     * @param height ожидаемая высота окна
     */
    void shouldHaveHeight(String height);

    /**
     * Закрытие окна через иконку крестика
     */
    void close();

    /**
     * Закрытие окна через нажатия клавишу Esc
     */
    void closeByEsc();

    /**
     * Клик по фону вне окна для закрытия
     */
    void clickBackdrop();

    /**
     * Проверка того, что нижняя часть окна зафиксирована
     */
    void shouldHaveFixedFooter();

    /**
     * Проверка того, что нижняя часть окна не зафиксирована
     */
    void shouldNotHaveFixedFooter();

    /**
     * Прокрутка окна в самый верх
     */
    void scrollUp();

    /**
     * Прокрутка окна в самый низ
     */
    void scrollDown();

    enum Placement {
        left, top, bottom, right
    }

    interface DrawerToolbar extends Modal.ModalToolbar {

    }
}

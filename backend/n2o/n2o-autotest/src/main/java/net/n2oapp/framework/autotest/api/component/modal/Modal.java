package net.n2oapp.framework.autotest.api.component.modal;

import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.page.Page;

/**
 * Модальная страница для автотестирования
 */
public interface Modal extends Component {

    /**
     * @return Панель кнопок для автотестирования
     */
    ModalToolbar toolbar();

    /**
     * Возвращает страницу внутри модальной страницы
     * @param pageClass тип возвращаемой страницы
     * @return компонент страница для автотестирования
     */
    <T extends Page> T content(Class<T> pageClass);

    /**
     * Проверка заголовка на соответствие
     * @param text ожидаемый заголовок
     */
    void shouldHaveTitle(String text);

    /**
     * Проверка отсутствия шапки
     */
    void shouldNotHaveHeader();

    /**
     * Прокрутка страницы в самый верх
     */
    void scrollUp();

    /**
     * Прокрутка страницы в самый верх
     */
    void scrollDown();

    /**
     * Закрытие модального окна через кнопку
     */
    void close();

    /**
     * Закрытие модального окна через нажатие клавишу Esc
     */
    void closeByEsc();

    /**
     * Проверка наличия возможности прокрутки окна
     */
    void shouldBeScrollable();

    /**
     * Проверка отсутствия возможности прокрутки окна
     */
    void shouldNotBeScrollable();

    /**
     * Клик на фон за модальным окном
     */
    void clickBackdrop();

    /**
     * Тулбар модального окна
     */
    interface ModalToolbar {
        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}

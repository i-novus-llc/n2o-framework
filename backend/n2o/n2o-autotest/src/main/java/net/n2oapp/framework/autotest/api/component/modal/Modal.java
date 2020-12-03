package net.n2oapp.framework.autotest.api.component.modal;

import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.page.Page;

/**
 * Модальная страница для автотестирования
 */
public interface Modal extends Component {

    ModalToolbar toolbar();

    <T extends Page> T content(Class<T> pageClass);

    void shouldHaveTitle(String text);

    void shouldNotHaveHeader();

    void scrollUp();

    void scrollDown();

    void close();

    void shouldBeScrollable();

    void shouldNotBeScrollable();

    void shouldHaveCssClass(String cssClass);

    void shouldHaveStyle(String style);

    void clickBackdrop();

    interface ModalToolbar {
        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}

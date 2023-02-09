package net.n2oapp.framework.autotest.api.component.drawer;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.Page;

/**
 * Окно drawer для автотестирования
 */
public interface Drawer extends Component {

    <T extends Page> T content(Class<T> pageClass);

    DrawerToolbar toolbar();

    void shouldHaveTitle(String text);

    void shouldHavePlacement(Placement placement);

    void shouldHaveWidth(String width);

    void shouldHaveHeight(String height);

    void close();

    void closeByEsc();

    void clickBackdrop();

    void shouldHaveFixedFooter();

    void shouldNotHaveFixedFooter();

    void scrollUp();

    void scrollDown();

    enum Placement {
        left, top, bottom, right
    }

    interface DrawerToolbar extends Modal.ModalToolbar {

    }
}

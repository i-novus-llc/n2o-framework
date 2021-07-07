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

    void placementShouldBe(Placement placement);

    void widthShouldBe(String width);

    void heightShouldBe(String height);

    void close();

    void closeByEsc();

    void clickBackdrop();

    void footerShouldBeFixed();

    void footerShouldNotBeFixed();

    void scrollUp();

    void scrollDown();

    enum Placement {
        left, top, bottom, right
    }

    interface DrawerToolbar extends Modal.ModalToolbar {

    }
}

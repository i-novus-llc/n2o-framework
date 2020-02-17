package net.n2oapp.framework.autotest.api.component.modal;

import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.page.Page;

public interface Modal extends Component {

    ModalToolbar toolbar();

    <T extends Page> T content(Class<T> pageClass);

    void shouldHaveTitle(String text);

    void close();

    interface ModalToolbar {
        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}

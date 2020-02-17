package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;

public interface Modal extends Component {

    ModalToolbar toolbar();

    <T extends Page> T page(Class<T> pageClass);

    void shouldHaveTitle(String text);

    void close();

    interface ModalToolbar {
        Toolbar bottomLeft();

        Toolbar bottomRight();
    }
}

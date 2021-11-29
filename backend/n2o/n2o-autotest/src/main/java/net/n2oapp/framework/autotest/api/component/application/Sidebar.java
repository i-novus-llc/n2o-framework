package net.n2oapp.framework.autotest.api.component.application;

import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент боковой панели для автотестирования
 */
public interface Sidebar extends Component {

    void brandNameShouldBe(String brandName);

    void brandLogoShouldBe(String brandName);

    void shouldBeFixed();

    void shouldBeRight();

    void shouldBeOverlay();

    void shouldHaveState(SidebarState state);

    void toggle();

    Menu nav();

    Menu extra();

}

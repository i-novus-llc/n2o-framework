package net.n2oapp.framework.autotest.api.component.drawer;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.page.Page;

public interface Drawer extends Component {

    <T extends Page> T content(Class<T> pageClass);

    void shouldHaveTitle(String text);

    void placementShouldBe(Placement placement);

    void widthShouldBe(String width);

    void heightShouldBe(String height);

    void close();

    enum Placement {
        left, top, bottom, right
    }
}

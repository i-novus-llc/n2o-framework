package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.Component;

public interface Cell extends Component {
    void shouldBeEmpty();

    void shouldBeExpandable();

    void shouldNotBeExpandable();

    void shouldBeExpanded();

    void shouldNotBeExpanded();

    void clickExpand();

    void shouldHaveIcon(String icon);

    void shouldNotHaveIcon();

    void shouldHaveStyle(String style);

    void shouldHaveCssClass(String cssClass);
}

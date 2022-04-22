package net.n2oapp.framework.autotest.impl.component.widget.html;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.html.HtmlWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

public class N2oHtmlWidget extends N2oStandardWidget implements HtmlWidget {

    @Override
    public void shouldHaveElement(String cssSelector) {
        element().$("div").$(cssSelector).shouldBe(Condition.exist);
    }

}


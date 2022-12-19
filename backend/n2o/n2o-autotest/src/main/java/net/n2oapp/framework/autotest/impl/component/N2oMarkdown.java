package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Markdown;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

public class N2oMarkdown extends N2oComponent implements Markdown {
    @Override
    public StandardButton markdownBtn(String label) {
        return N2oSelenide.component(element().$$(".n2o-markdown-button").findBy(Condition.text(label)), StandardButton.class);
    }
}

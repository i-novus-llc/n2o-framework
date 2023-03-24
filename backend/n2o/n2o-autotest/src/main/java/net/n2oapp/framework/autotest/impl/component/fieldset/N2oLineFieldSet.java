package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.fieldset.LineFieldSet;

/**
 * Филдсет с горизонтальным делителем для автотестирования
 */
public class N2oLineFieldSet extends N2oFieldSet implements LineFieldSet {

    @Override
    public Fields fields() {
        return N2oSelenide.collection(element().$$(".n2o-form-group"), Fields.class);
    }

    @Override
    public void shouldBeEmpty() {
        content().shouldHave(Condition.empty);
    }

    @Override
    public void shouldBeCollapsible() {
        header().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeCollapsible() {
        header().shouldNot(Condition.exist);
    }

    @Override
    public void shouldHaveLabel(String label) {
        SelenideElement labelElement = header().exists() ? panelHeaderText() : fieldsetTitleText();

        labelElement.shouldHave(Condition.text(label));
    }

    @Override
    public void shouldNotHaveLabel() {
        if (header().exists())
            panelHeaderText().shouldHave(Condition.empty);
        else
            fieldsetTitleText().shouldNotBe(Condition.exist);
    }

    @Override
    public void expand() {
        if (!item().is(expandedContentCondition()))
            header().click();
    }

    @Override
    public void collapse() {
        if (item().is(expandedContentCondition()))
            header().click();
    }

    @Override
    public void shouldBeExpanded() {
        item().shouldBe(expandedContentCondition());
    }

    @Override
    public void shouldBeCollapsed() {
        item().shouldNotBe(expandedContentCondition());
    }

    protected SelenideElement header() {
        return element().$(".n2o-panel-header");
    }

    protected SelenideElement item() {
        return element().$(".rc-collapse-item");
    }

    protected SelenideElement content() {
        return element().$(".rc-collapse-content-box");
    }

    private Condition expandedContentCondition() {
        return Condition.cssClass("rc-collapse-item-active");
    }

    protected SelenideElement fieldsetTitleText() {
        return element().$(".title-fieldset-text");
    }

    protected SelenideElement panelHeaderText() {
        return header().$(".n2o-panel-header-text");
    }
}

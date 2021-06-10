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
        SelenideElement elm = header().exists() ?
                header().$(".n2o-panel-header-text") :
                element().$(".title-fieldset-text");
        elm.shouldHave(Condition.text(label));
    }

    @Override
    public void shouldNotHaveLabel() {
        if (header().exists())
            header().$(".n2o-panel-header-text").shouldHave(Condition.empty);
        else
            element().$(".title-fieldset-text").shouldNotBe(Condition.exist);
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

    private SelenideElement header() {
        return element().$(".n2o-panel-header");
    }

    private SelenideElement item() {
        return element().$(".rc-collapse-item");
    }

    private Condition expandedContentCondition() {
        return Condition.cssClass("rc-collapse-item-active");
    }

    private SelenideElement content() {
        return element().$(".rc-collapse-content-box");
    }
}

package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.fieldset.LineFieldSet;

import java.time.Duration;

/**
 * Филдсет с горизонтальным делителем для автотестирования
 */
public class N2oLineFieldSet extends N2oFieldSet implements LineFieldSet {

    public static final String COLLAPSE_PANEL_CONTENT_ACTIVE = ".collapse-panel-content-active";
    public static final String COLLAPSE_PANEL_CONTENT_INACTIVE = ".collapse-panel-content-inactive";

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
    public void shouldHaveLabel(String label, Duration... duration) {
        SelenideElement labelElement = header().exists() ? panelHeaderText() : fieldsetTitleText();

        should(Condition.text(label), labelElement, duration);
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
        if (element().$(COLLAPSE_PANEL_CONTENT_INACTIVE).exists())
            header().click();
    }

    @Override
    public void collapse() {
        if (element().$(COLLAPSE_PANEL_CONTENT_ACTIVE).exists())
            header().click();
    }

    @Override
    public void shouldBeExpanded() {
        item().$(COLLAPSE_PANEL_CONTENT_ACTIVE).shouldBe(Condition.exist);
    }

    @Override
    public void shouldBeCollapsed() {
        item().$(COLLAPSE_PANEL_CONTENT_INACTIVE).shouldBe(Condition.exist);
    }

    protected SelenideElement header() {
        return element().$(".n2o-panel-header");
    }

    protected SelenideElement item() {
        return element().$(".collapse-panel");
    }

    protected SelenideElement content() {
        return element().$(".collapse-panel-content-box");
    }

    private WebElementCondition expandedContentCondition() {
        return Condition.cssClass("collapse-panel-content-active");
    }

    protected SelenideElement fieldsetTitleText() {
        return element().$(".title-fieldset-text");
    }

    protected SelenideElement panelHeaderText() {
        return header().$(".n2o-panel-header-text");
    }
}

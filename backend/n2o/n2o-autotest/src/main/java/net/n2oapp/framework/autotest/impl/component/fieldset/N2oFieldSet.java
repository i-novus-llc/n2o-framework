package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.fieldset.FieldSet;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Филдсет для автотестирования
 */
public abstract class N2oFieldSet extends N2oComponent implements FieldSet {

    @Deprecated
    public void shouldNotBeVisible() {
        shouldBeHidden();
    }

    @Override
    public void shouldHaveDescription(String description) {
        description().shouldHave(Condition.text(description));
    }

    @Override
    public void shouldNotHaveDescription() {
        description().shouldNot(Condition.exist);
    }

    protected SelenideElement description() {
        return element().$(".n2o-fieldset__description");
    }
}

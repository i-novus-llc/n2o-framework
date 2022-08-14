package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.Badge;

public class N2oBadge extends N2oComponent implements Badge {
    @Override
    public void shouldHaveImage() {
        image().shouldBe(Condition.exist);
    }

    @Override
    public void shouldBeShape(String shape) {
        element().shouldHave(Condition.cssClass(String.format("n2o-badge_%s", shape)));
    }

    @Override
    public void imageShouldBeShape(String shape) {
        image().shouldHave(Condition.cssClass(String.format("n2o-badge-image--%s", shape)));
    }

    @Override
    public void imageShouldBePosition(String position) {
        image().shouldHave(Condition.cssClass(String.format("n2o-badge-image_%s", position)));
    }

    private SelenideElement image() {
        return element().$(".n2o-badge-image");
    }
}

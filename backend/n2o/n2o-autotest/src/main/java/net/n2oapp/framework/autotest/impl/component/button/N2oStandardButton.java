package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.Arrays;

/**
 * Стандартная кнопка для автотестирования
 */
public class N2oStandardButton extends N2oButton implements StandardButton {

    @Override
    public void shouldHaveLabel(String label, Duration... duration) {
        should(Condition.exactText(label), duration);
    }

    @Override
    public void shouldHaveDescription(String description, Duration... duration) {
        element().shouldHave(Condition.attribute("hint", description));
    }

    @Override
    public void shouldHaveIcon(String iconName) {
        Arrays.stream(iconName.split(" ")).forEach(i -> element().$("i").shouldHave(Condition.cssClass(i)));
    }

    @Override
    public void shouldNotHaveIcon() {
        element().$("i").shouldNot(Condition.exist);
    }

    @Override
    public void badgeShouldHavePosition(BadgePosition position) {
        shouldHaveCssClass(position.name("btn-badge-position--"));
    }

    @Override
    public void shouldBeRounded() {
        element().shouldHave(Condition.cssClass("btn-rounded__with-content"));
    }

    @Override
    public void shouldNotBeRounded() {
        element().shouldNotHave(Condition.cssClass("btn-rounded__with-content"));
    }

    @Override
    public void shouldBeDraggable() {
        dndElement().shouldHave(Condition.attribute("data-draggable", "true"));
    }

    @Override
    public void shouldNotBeDraggable() {
        dndElement().shouldNotHave(Condition.attribute("data-draggable", "true"));
    }

    @Override
    public void shouldHaveDndIcon() {
        dndElement().$(".fa-ellipsis-v").should(Condition.exist);
    }

    @Override
    public void shouldNotHaveDndIcon() {
        dndElement().$(".fa-ellipsis-v").shouldNot(Condition.exist);
    }

    @Override
    public void dragAndDropTo(StandardButton standardButton) {
        Actions actions = new Actions(Selenide.webdriver().driver().getWebDriver());
        actions.clickAndHold(dndElement())
                .moveToElement(standardButton.element())
                .release()
                .perform();
    }

    protected SelenideElement dndElement() {
        return element().parent().$(".drag-handle");
    }
}
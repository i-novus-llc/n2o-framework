package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.Slider;
import org.openqa.selenium.Keys;

/**
 * Компонент ползунок для автотестирования
 */
public class N2oSlider extends N2oControl implements Slider {

    @Override
    public void shouldBeEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(String value) {
        setValue(sliderElement(0), value, 1);
    }

    @Override
    public void setValue(String value, int step) {
        setValue(sliderElement(0), value, step);
    }

    @Override
    public void setLeftValue(String value) {
        setValue(value);
    }

    @Override
    public void setLeftValue(String value, int step) {
        setValue(value, step);
    }

    @Override
    public void setRightValue(String value) {
        setValue(sliderElement(1), value, 1);
    }

    @Override
    public void setRightValue(String value, int step) {
        setValue(sliderElement(1), value, step);
    }

    @Override
    public void shouldHaveValue(String value) {
        shouldHaveValue(sliderElement(0), value);
    }

    @Override
    public void shouldHaveLeftValue(String value) {
        shouldHaveValue(value);
    }

    @Override
    public void shouldHaveRightValue(String value) {
        shouldHaveValue(sliderElement(1), value);
    }

    private void shouldHaveValue(SelenideElement element, String value) {
        element.shouldHave(Condition.attribute("aria-valuenow", value));
    }

    protected SelenideElement sliderElement(int index) {
        return element().$$(".rc-slider-handle").get(index).shouldHave(Condition.exist);
    }

    private void setValue(SelenideElement element, String value, int step) {
        String current = element.getAttribute("aria-valuenow");
        int dif = (Integer.parseInt(current) - Integer.parseInt(value)) / step;
        Keys keys = dif > 0 ? Keys.ARROW_LEFT : Keys.ARROW_RIGHT;
        element.click();
        for (int i = 0; i < Math.abs(dif); i++)
            element.sendKeys(keys);
    }
}

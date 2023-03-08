package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.chart.Area;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Область виджета диаграмма-область для автотестирования
 */
public class N2oArea extends N2oComponent implements Area {

    @Override
    public void shouldHaveWidth(int width) {
        path().shouldHave(Condition.attribute("width", String.valueOf(width)));
    }

    @Override
    public void shouldHaveHeight(int height) {
        path().shouldHave(Condition.attribute("height", String.valueOf(height)));
    }

    protected SelenideElement path() {
        return element().$("path");
    }
}

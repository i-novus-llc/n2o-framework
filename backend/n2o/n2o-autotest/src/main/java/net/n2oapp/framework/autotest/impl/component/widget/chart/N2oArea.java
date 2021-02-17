package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.chart.Area;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Реализация области виджета диаграмма-область для автотестирования
 */
public class N2oArea extends N2oComponent implements Area {


    @Override
    public void shouldHaveWidth(String width) {
        element().$("path").shouldHave(Condition.attribute("width", width));
    }

    @Override
    public void shouldHaveHeight(String height) {
        element().$("path").shouldHave(Condition.attribute("height", height));
    }
}

package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.chart.Area;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Область виджета диаграмма-область для автотестирования
 */
public class N2oArea extends N2oComponent implements Area {

    @Override
    public void shouldHaveColor(String color) {
        element().$("path.recharts-area-area").shouldHave(Condition.attribute("fill", color));
    }

    @Override
    public void shouldHaveStrokeColor(String color) {
        element().$("path.recharts-area-curve").shouldHave(Condition.attribute("stroke", color));
    }
}
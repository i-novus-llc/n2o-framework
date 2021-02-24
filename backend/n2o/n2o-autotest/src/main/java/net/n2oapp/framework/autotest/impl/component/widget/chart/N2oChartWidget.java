package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.chart.ChartWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oWidget;

/**
 * Реализация виджета диаграмма для автотестирования
 */
public abstract class N2oChartWidget extends N2oWidget implements ChartWidget {

    @Override
    public void shouldHaveWidth(int width) {
        element().$(".recharts-wrapper").shouldHave(Condition.cssValue("width", width + "px"));
    }

    @Override
    public void shouldHaveHeight(int height) {
        element().$(".recharts-wrapper").shouldHave(Condition.cssValue("height", height + "px"));
    }
}

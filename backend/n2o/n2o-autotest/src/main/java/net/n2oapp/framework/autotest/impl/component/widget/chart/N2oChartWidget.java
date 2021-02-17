package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.chart.AreaChartWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

/**
 * Реализация виджета диаграмма для автотестирования
 */
public abstract class N2oChartWidget extends N2oStandardWidget implements AreaChartWidget {

    @Override
    public void shouldHaveWidth(String width) {
        element().$(".recharts-wrapper").shouldHave(Condition.cssValue("width", width));
    }

    @Override
    public void shouldHaveHeight(String height) {
        element().$(".recharts-wrapper").shouldHave(Condition.cssValue("height", height));
    }
}

package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.chart.ChartWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oWidget;

/**
 * Реализация виджета диаграмма для автотестирования
 */
public abstract class N2oChartWidget extends N2oWidget implements ChartWidget {

    @Override
    public void shouldHaveWidth(int width) {
        chart().shouldHave(Condition.cssValue("width", String.format("%dpx", width)));
    }

    @Override
    public void shouldHaveHeight(int height) {
        chart().shouldHave(Condition.cssValue("height", String.format("%dpx", height)));
    }

    protected SelenideElement chart() {
        return element().$(".recharts-wrapper");
    }
}

package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.CollectionCondition;
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

    @Override
    public void shouldHaveXAxis() {
        element().$(".recharts-xAxis").should(Condition.exist);
    }

    @Override
    public void shouldHaveYAxis() {
        element().$(".recharts-yAxis").should(Condition.exist);
    }

    @Override
    public void shouldHaveGrid() {
        element().$(".recharts-cartesian-grid").should(Condition.exist);
    }

    @Override
    public void shouldHaveLegend() {
        element().$(".recharts-legend-wrapper").should(Condition.exist);
    }

    @Override
    public void shouldHaveChartWidth(int width) {
        // plot area задаётся clipPath rect внутри chart (один на серию, размеры одинаковые)
        element().$$("clipPath rect")
                .filter(Condition.attribute("width", String.valueOf(width)))
                .shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Override
    public void shouldHaveChartHeight(int height) {
        element().$$("clipPath rect")
                .filter(Condition.attribute("height", String.valueOf(height)))
                .shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Override
    public void shouldHaveLegendItem(int index, String text) {
        element().$$(".recharts-legend-item-text")
                .get(index)
                .shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveXAxisTick(int index, String text) {
        element().$$(".recharts-xAxis .recharts-cartesian-axis-tick-value")
                .filter(Condition.not(Condition.empty))
                .get(index)
                .shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveXAxisTicksCount(int count) {
        element().$$(".recharts-xAxis .recharts-cartesian-axis-tick-value")
                .filter(Condition.not(Condition.empty))
                .shouldHave(CollectionCondition.size(count));
    }

    @Override
    public void shouldHaveYAxisTick(int index, String text) {
        element().$$(".recharts-yAxis .recharts-cartesian-axis-tick-value")
                .filter(Condition.not(Condition.empty))
                .get(index)
                .shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveYAxisTicksCount(int count) {
        element().$$(".recharts-yAxis .recharts-cartesian-axis-tick-value")
                .filter(Condition.not(Condition.empty))
                .shouldHave(CollectionCondition.size(count));
    }

    protected SelenideElement chart() {
        return element().$(".recharts-wrapper");
    }
}
package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.chart.Pie;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oPie extends N2oComponent implements Pie {

    @Override
    public void shouldHaveSectorsCount(int count) {
        element().$$(".recharts-sector").shouldHave(CollectionCondition.size(count));
    }

    @Override
    public void shouldHaveSectorColor(String color) {
        element().$$(".recharts-sector")
                .shouldHave(CollectionCondition.allMatch(
                        "each sector has color " + color,
                        el -> color.equalsIgnoreCase(el.getAttribute("fill"))));
    }

    @Override
    public void shouldHaveLabels() {
        element().$$(".recharts-pie-label-text").shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Override
    public void shouldHaveLabelText(String text) {
        element().$$(".recharts-pie-label-text").shouldHave(CollectionCondition.itemWithText(text));
    }
}
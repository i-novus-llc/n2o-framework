package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.chart.Bar;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oBar extends N2oComponent implements Bar {
    @Override
    public void shouldHaveColor(String color) {
        element().$(".recharts-rectangle").shouldHave(Condition.attribute("fill", color));
    }
}
package net.n2oapp.framework.autotest.impl.component.widget.chart;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.chart.Line;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oLine extends N2oComponent implements Line {
    @Override
    public void shouldHaveColor(String color) {
        element().$(".recharts-line-curve").shouldHave(Condition.attribute("stroke", color));
    }
}
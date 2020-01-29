package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

/**
 * Клиентская модель диаграммы
 */
@Getter
@Setter
public class Chart extends Widget<ChartWidgetComponent> {
    public Chart() {
        super(new ChartWidgetComponent());
    }

    @JsonProperty("chart")
    @Override
    public ChartWidgetComponent getComponent() {
        return super.getComponent();
    }
}

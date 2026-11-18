package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAreaChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oBarChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oLineChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oStandardChartItem;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор виджета-диаграммы
 */
@Component
public class ChartValidator extends WidgetValidator<N2oChart> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oChart.class;
    }

    @Override
    public void validate(N2oChart source, SourceProcessor p) {
        super.validate(source, p);
        if (source.getComponent() instanceof N2oAreaChart areaChart)
            checkItemsFieldId(areaChart.getItems(), "area", source);
        else if (source.getComponent() instanceof N2oBarChart barChart)
            checkItemsFieldId(barChart.getItems(), "bar", source);
        else if (source.getComponent() instanceof N2oLineChart lineChart)
            checkItemsFieldId(lineChart.getItems(), "line", source);
    }

    /**
     * Проверка наличия обязательного атрибута field-id у элементов диаграммы
     *
     * @param items  Элементы диаграммы
     * @param tag    Название тега элемента диаграммы
     * @param source Исходная модель виджета-диаграммы
     */
    private void checkItemsFieldId(N2oStandardChartItem[] items, String tag, N2oChart source) {
        if (items == null)
            return;
        for (N2oStandardChartItem item : items) {
            if (item.getFieldId() == null || item.getFieldId().isBlank())
                throw new N2oMetadataValidationException(
                        String.format("В элементе \"<%s>\" диаграммы виджета %s не задан обязательный атрибут 'field-id'",
                                tag, ValidationUtils.getIdOrEmptyString(source.getId())));
        }
    }
}

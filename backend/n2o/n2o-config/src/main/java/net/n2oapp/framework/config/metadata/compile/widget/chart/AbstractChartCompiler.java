package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartWidgetComponent;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import org.springframework.stereotype.Component;

/**
 * Компиляция абстрактного компонента диаграммы
 */
@Component
public abstract class AbstractChartCompiler<D extends ChartWidgetComponent, S extends N2oAbstractChart> implements BaseSourceCompiler<D, S, CompileContext<?,?>> {

    protected D build(D compiled, S source, CompileProcessor p, String defaultSrc) {
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(defaultSrc, String.class)));
        return compiled;
    }
}

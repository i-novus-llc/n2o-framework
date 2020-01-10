package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

public abstract class AbstractChartCompiler<D extends Chart, S extends N2oChart> implements BaseSourceCompiler<D, S, CompileContext<?,?>> {

    protected D build(D compiled, S source, CompileContext<?,?> context, CompileProcessor p, String defaultSrc) {
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(defaultSrc, String.class)));
        return compiled;
    }
}

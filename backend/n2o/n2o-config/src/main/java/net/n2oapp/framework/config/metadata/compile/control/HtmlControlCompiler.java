package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import net.n2oapp.framework.api.metadata.meta.control.HtmlControl;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;


/**
 * Компиляция компонента вывода html
 */
@Component
public class HtmlControlCompiler extends StandardFieldCompiler<HtmlControl, N2oHtml> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.html.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oHtml.class;
    }

    @Override
    public StandardField<HtmlControl> compile(N2oHtml source, CompileContext<?, ?> context, CompileProcessor p) {
        HtmlControl htmlControl = new HtmlControl();
        htmlControl.setHtml(source.getHtml());
        return compileStandardField(htmlControl, source, context, p);
    }
}

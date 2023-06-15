package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import net.n2oapp.framework.api.metadata.meta.control.Html;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.hasLink;


/**
 * Компиляция компонента вывода html
 */
@Component
public class HtmlCompiler extends FieldCompiler<Html, N2oHtml> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.html.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oHtml.class;
    }

    @Override
    public Html compile(N2oHtml source, CompileContext<?, ?> context, CompileProcessor p) {
        Html html = new Html();
        if (source.getHtml() != null) {
            String content = source.getHtml();
            if (hasLink(content))
                content = content.replace("'", "\\\'");
            html.setHtml(p.resolveJS(content.trim()));
        }
        initDefaults(source, context, p);
        compileField(html, source, context, p);
        return html;
    }
}

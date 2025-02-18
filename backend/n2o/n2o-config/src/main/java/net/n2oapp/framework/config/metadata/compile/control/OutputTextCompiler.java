package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oOutputText;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.api.metadata.meta.control.OutputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;


/**
 * Компиляция компонента вывода однострочного текста
 */
@Component
public class OutputTextCompiler extends StandardFieldCompiler<OutputText, N2oOutputText> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.output_text.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOutputText.class;
    }

    @Override
    public StandardField<OutputText> compile(N2oOutputText source, CompileContext<?, ?> context, CompileProcessor p) {
        OutputText outputText = new OutputText();
        outputText.setIconPosition(castDefault(source.getIconPosition(),
                () -> p.resolve(property("n2o.api.control.output_text.icon_position"), Position.class)));
        outputText.setIcon(p.resolveJS(source.getIcon()));
        outputText.setFormat(p.resolveJS(source.getFormat()));
        return compileStandardField(outputText, source, context, p);
    }
}

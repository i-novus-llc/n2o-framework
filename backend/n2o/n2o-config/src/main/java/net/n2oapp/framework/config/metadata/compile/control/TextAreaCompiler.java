package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oTextArea;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.TextArea;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция многосточного текстового поля
 */
@Component
public class TextAreaCompiler extends StandardFieldCompiler<TextArea, N2oTextArea> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTextArea.class;
    }

    @Override
    public StandardField<TextArea> compile(N2oTextArea source, CompileContext<?, ?> context, CompileProcessor p) {
        TextArea textArea = new TextArea();
        textArea.setControlSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.text_area.src"), String.class)));
        textArea.setRows(source.getRows());
        return compileStandardField(textArea, source, context, p);
    }
}
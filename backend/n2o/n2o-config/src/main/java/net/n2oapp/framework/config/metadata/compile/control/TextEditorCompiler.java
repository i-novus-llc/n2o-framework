package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oTextEditor;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.TextEditor;
import org.springframework.stereotype.Component;


/**
 * Компиляция редактора текста
 */
@Component
public class TextEditorCompiler extends StandardFieldCompiler<TextEditor, N2oTextEditor> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.text_editor.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTextEditor.class;
    }

    @Override
    public StandardField<TextEditor> compile(N2oTextEditor source, CompileContext<?, ?> context, CompileProcessor p) {
        TextEditor textEditor = new TextEditor();
        textEditor.setName(p.resolveJS(source.getLabel()));
        return compileStandardField(textEditor, source, context, p);
    }
}

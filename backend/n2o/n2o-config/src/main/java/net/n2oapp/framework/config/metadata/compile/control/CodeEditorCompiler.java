package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeEditor;
import net.n2oapp.framework.api.metadata.meta.control.CodeEditor;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция контрола с возможностью редактирования кода
 */
@Component
public class CodeEditorCompiler extends StandardFieldCompiler<CodeEditor, N2oCodeEditor> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCodeEditor.class;
    }

    @Override
    public StandardField<CodeEditor> compile(N2oCodeEditor source, CompileContext<?, ?> context, CompileProcessor p) {
        CodeEditor codeEditor = new CodeEditor();
        codeEditor.setControlSrc(p.cast(codeEditor.getControlSrc(), p.resolve(property("n2o.api.control.code_editor.src"), String.class)));
        codeEditor.setName(p.resolveJS(source.getLabel()));
        codeEditor.setLang(source.getLanguage());
        codeEditor.setAutocomplete(true);
        codeEditor.setMinLines(1);
        codeEditor.setMaxLines(source.getRows());
        return compileStandardField(codeEditor, source, context, p);
    }
}

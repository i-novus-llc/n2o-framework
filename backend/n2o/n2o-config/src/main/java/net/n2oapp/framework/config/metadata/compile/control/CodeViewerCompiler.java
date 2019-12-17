package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeViewer;
import net.n2oapp.framework.api.metadata.meta.control.CodeViewer;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

/**
 * Компиляция контрола просмотра кода
 */
@Component
public class CodeViewerCompiler extends StandardFieldCompiler<CodeViewer, N2oCodeViewer> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCodeViewer.class;
    }

    @Override
    public StandardField<CodeViewer> compile(N2oCodeViewer source, CompileContext<?, ?> context, CompileProcessor p) {
        CodeViewer codeViewer = new CodeViewer();
        codeViewer.setTitle(source.getTitle());
        codeViewer.setLanguage(source.getLanguage());
        codeViewer.setTheme(source.getTheme());
        codeViewer.setHide(source.getHide());
        codeViewer.setShowLineNumbers(source.getShowLineNumbers());
        codeViewer.setStartingLineNumber(source.getStartingLineNumber());
        return compileStandardField(codeViewer, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.code_viewer.src";
    }
}

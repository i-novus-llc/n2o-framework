package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeViewer;
import net.n2oapp.framework.api.metadata.meta.control.CodeViewer;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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
        codeViewer.setText(source.getText().trim().replaceAll("\n( )+", "\n"));
        codeViewer.setLanguage(source.getLanguage());
        codeViewer.setDarkTheme(source.getTheme() != null && source.getTheme().equals(N2oCodeViewer.ColorTheme.dark));
        codeViewer.setShowLineNumbers(p.cast(source.getShowLineNumbers(),
                p.resolve(property("n2o.api.control.code.show_line_numbers"), Boolean.class)));
        codeViewer.setStartingLineNumber(p.cast(source.getStartingLineNumber(),
                p.resolve(property("n2o.api.control.code.starting_line_number"), Integer.class)));
        codeViewer.setHideButtons(p.cast(source.getHideButtons(),
                p.resolve(property("n2o.api.control.code.hide_buttons"), Boolean.class)));
        codeViewer.setHideOverflow(p.cast(source.getHideOverflow(),
                p.resolve(property("n2o.api.control.code.hide_overflow"), Boolean.class)));
        return compileStandardField(codeViewer, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.code_viewer.src";
    }
}

package net.n2oapp.framework.config.metadata.compile.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.plain.N2oTextEditor;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.TextEditor;
import net.n2oapp.framework.config.register.storage.PathUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


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
        compileToolbar(source, textEditor, p);
        return compileStandardField(textEditor, source, context, p);
    }

    private void compileToolbar(N2oTextEditor source, TextEditor compiled, CompileProcessor p) {
        String toolbarUrl = p.cast(source.getToolbarUrl(),
                p.resolve(Placeholders.property("n2o.api.control.text_editor.toolbar_url"), String.class));
        if (toolbarUrl == null) return;
        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        ObjectMapper mapper = new ObjectMapper();
        Map toolbarConfig;
        try {
            InputStream is = r.getResource(PathUtil.convertPathToClasspathUri(toolbarUrl)).getInputStream();
            toolbarConfig = mapper.readValue(is, Map.class);
        } catch (IOException e) {
            throw new N2oException(e);
        }
        compiled.setToolbarConfig(toolbarConfig);
    }
}

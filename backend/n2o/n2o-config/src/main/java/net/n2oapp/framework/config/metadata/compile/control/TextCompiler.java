package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oText;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import org.springframework.stereotype.Component;

/**
 * Компиляция TextField
 */

@Component
public class TextCompiler extends ComponentCompiler<Text, N2oText> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oText.class;
    }

    @Override
    public Text compile(N2oText source, CompileContext<?, ?> context, CompileProcessor p) {
        Text text = new Text();
        text.setText(source.getText());
        text.setFormat(source.getFormat());

        compileComponent(text, source, context, p);

        return text;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.text_field.src";
    }
}

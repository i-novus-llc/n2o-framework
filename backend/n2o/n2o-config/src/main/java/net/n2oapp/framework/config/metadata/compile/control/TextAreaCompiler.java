package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oTextArea;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.TextArea;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;


/**
 * Компиляция компонента ввода многострочного текста
 */
@Component
public class TextAreaCompiler extends StandardFieldCompiler<TextArea, N2oTextArea> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.text_area.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTextArea.class;
    }

    @Override
    public StandardField<TextArea> compile(N2oTextArea source, CompileContext<?, ?> context, CompileProcessor p) {
        TextArea textArea = new TextArea();
        textArea.setMinRows(castDefault(source.getMinRows(), () -> getMinRowsFromProperties(p)));
        textArea.setMaxRows(castDefault(source.getMaxRows(),
                () -> p.resolve(property("n2o.api.control.text_area.max_rows"), Integer.class)));
        return compileStandardField(textArea, source, context, p);
    }

    private Integer getMinRowsFromProperties(CompileProcessor p) {
        ComponentScope columnScope = p.getScope(ComponentScope.class);
        if (columnScope != null && columnScope.unwrap(N2oSimpleColumn.class) != null)
            return p.resolve(property("n2o.api.cell.edit.text_area.min_rows"), Integer.class);
        return p.resolve(property("n2o.api.control.text_area.min_rows"), Integer.class);
    }
}

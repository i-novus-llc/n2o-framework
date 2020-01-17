package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Компиляция колонки филдсета
 */
@Component
public class FieldSetColumnCompiler implements BaseSourceCompiler<FieldSet.Column, N2oFieldsetColumn, CompileContext<?, ?>> {

    @Override
    public FieldSet.Column compile(N2oFieldsetColumn source, CompileContext<?, ?> context, CompileProcessor p) {
        FieldSet.Column column = new FieldSet.Column();
        column.setClassName(source.getCssClass());
        column.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        column.setSize(source.getSize());
        column.setVisible(
                source.getVisible() != null && source.getVisible().startsWith("{") && source.getVisible().endsWith("}") ?
                        "`" + source.getVisible().substring(1, source.getVisible().length() - 1) + "`" :
                        source.getVisible()
        );

        if (source.getItems() != null && source.getItems().length > 0) {
            if (source.getItems()[0] instanceof N2oField) {
                List<Field> fields = new ArrayList<>();
                for (NamespaceUriAware item : source.getItems()) {
                    fields.add(p.compile(item, context));
                }
                if (fields.size() == 1)
                    column.setSize(column.getSize());
                column.setFields(fields);
            } else {
                List<FieldSet> fieldSets = new ArrayList<>();
                for (NamespaceUriAware item : source.getItems()) {
                    fieldSets.add(p.compile(item, context));
                }
                column.setFieldsets(fieldSets);
            }
        }
        return column;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetColumn.class;
    }
}

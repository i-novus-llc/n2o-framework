package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.script.ScriptProcessor;
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
        column.setVisible(ScriptProcessor.resolveExpression(source.getVisible()));

        if (source.getItems() != null && source.getItems().length > 0) {
            boolean onlyFields = true;
            for (SourceComponent item : source.getItems())
                if (!(item instanceof N2oField)) {
                    onlyFields = false;
                    break;
                }

            if (onlyFields) {
                List<Field> compiledFields = new ArrayList<>();
                for (SourceComponent field : source.getItems())
                    compiledFields.add(p.compile(field, context));
                column.setFields(compiledFields);
            } else {
                List<FieldSet> fieldSets = new ArrayList<>();
                int i = 0;
                while (i < source.getItems().length) {
                    N2oFieldSet fieldSet;
                    if (source.getItems()[i] instanceof N2oFieldSet) {
                        fieldSet = (N2oFieldSet) source.getItems()[i];
                        i++;
                    } else {
                        N2oSetFieldSet newFieldSet = new N2oSetFieldSet();
                        List<SourceComponent> fieldSetItems = new ArrayList<>();
                        while (i < source.getItems().length && !(source.getItems()[i] instanceof N2oFieldSet)) {
                            fieldSetItems.add(source.getItems()[i]);
                            i++;
                        }
                        SourceComponent[] items = new SourceComponent[fieldSetItems.size()];
                        newFieldSet.setItems(fieldSetItems.toArray(items));
                        fieldSet = newFieldSet;
                    }
                    fieldSets.add(p.compile(fieldSet, context));
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

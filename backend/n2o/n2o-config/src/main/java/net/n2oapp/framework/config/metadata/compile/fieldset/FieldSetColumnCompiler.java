package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.SetFieldSet;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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
            List<FieldSet> fieldSets = new ArrayList<>();
            List<FieldSet.Row> rows = new ArrayList<>();
            List<Field> fields = new ArrayList<>();
            for (NamespaceUriAware item : source.getItems()) {
                Compiled compiled = p.compile(item, context);
                if (compiled instanceof FieldSet) {
                    if (!rows.isEmpty())
                        fieldSets.add(createWrappingFieldsetUnderRows(rows));
                    else if (!fields.isEmpty())
                        fieldSets.add(createWrappingFieldsetUnderFields(fields));
                    fieldSets.add((FieldSet) compiled);
                } else {
                    if (compiled instanceof Field) {
                        if (!rows.isEmpty())
                            fieldSets.add(createWrappingFieldsetUnderRows(rows));
                        fields.add((Field) compiled);
                    } else if (compiled instanceof FieldSet.Row) {
                        if (!fields.isEmpty())
                            fieldSets.add(createWrappingFieldsetUnderFields(fields));
                        rows.add((FieldSet.Row) compiled);
                    }
                }
            }
            if (!rows.isEmpty())
                fieldSets.add(createWrappingFieldsetUnderRows(rows));
            else if (!fields.isEmpty())
                fieldSets.add(createWrappingFieldsetUnderFields(fields));

            column.setFieldsets(fieldSets);
        }
        return column;
    }

    /**
     * Создание филдсета, который будет оборачивать одну или несколько подряд идущих строк,
     * лежащих вне филдсетов
     *
     * @param rows Список строк
     * @return Филдсет, содержащий все входящие строки
     */
    private FieldSet createWrappingFieldsetUnderRows(List<FieldSet.Row> rows) {
        FieldSet fieldSet = new SetFieldSet();
        fieldSet.setRows(new ArrayList<>(rows));
        rows.clear();
        return fieldSet;
    }

    /**
     * Создание филдсета, который будет оборачивать одну или несколько подряд идущих полей,
     * лежащих вне филдсетов
     *
     * @param fields Список полей
     * @return Филдсет, содержащий все входящие поля
     */
    private FieldSet createWrappingFieldsetUnderFields(List<Field> fields) {
        FieldSet.Column column = new FieldSet.Column();
        column.setFields(fields);

        FieldSet.Row row = new FieldSet.Row();
        row.setCols(Collections.singletonList(column));

        FieldSet fieldSet = new SetFieldSet();
        fieldSet.setRows(Collections.singletonList(row));
        return fieldSet;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetColumn.class;
    }
}

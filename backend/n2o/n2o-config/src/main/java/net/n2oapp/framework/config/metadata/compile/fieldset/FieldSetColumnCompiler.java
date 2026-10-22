package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetCol;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Компиляция колонки филдсета
 */
@Component
public class FieldSetColumnCompiler implements BaseSourceCompiler<FieldSet.Column, N2oFieldsetCol, CompileContext<?, ?>> {

    @Override
    public FieldSet.Column compile(N2oFieldsetCol source, CompileContext<?, ?> context, CompileProcessor p) {
        FieldSet.Column column = new FieldSet.Column();
        column.setClassName(p.resolveJS(source.getCssClass()));
        column.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        column.setSize(source.getSize());
        column.setProperties(p.mapAttributes(source));
        column.setVisible(ScriptProcessor.resolveExpression(source.getVisible()));
        FieldSetVisibilityScope scope = initVisibilityScope(source, p);
        FieldsetItem[] items = source.getItems();
        if (items == null || items.length == 0)
            return column;

        if (Arrays.stream(items).allMatch(N2oField.class::isInstance)) {
            List<Field> compiledFields = new ArrayList<>();
            for (SourceComponent field : items)
                compiledFields.add(p.compile(field, context, scope));
            column.setFields(compiledFields);
        } else {
            List<FieldSet> fieldSets = new ArrayList<>();
            for (N2oFieldSet fieldSet : groupIntoFieldSets(items))
                fieldSets.add(p.compile(fieldSet, context, scope));
            column.setFieldsets(fieldSets);
        }
        return column;
    }

    /**
     * Группировка элементов колонки в филдсеты:
     * подряд идущие элементы, не являющиеся филдсетами, оборачиваются в N2oSetFieldSet
     */
    private List<N2oFieldSet> groupIntoFieldSets(FieldsetItem[] items) {
        List<N2oFieldSet> fieldSets = new ArrayList<>();
        List<FieldsetItem> pendingItems = new ArrayList<>();
        for (FieldsetItem item : items) {
            if (item instanceof N2oFieldSet fieldSet) {
                flushPendingItems(fieldSets, pendingItems);
                fieldSets.add(fieldSet);
            } else {
                pendingItems.add(item);
            }
        }
        flushPendingItems(fieldSets, pendingItems);
        return fieldSets;
    }

    private void flushPendingItems(List<N2oFieldSet> fieldSets, List<FieldsetItem> pendingItems) {
        if (pendingItems.isEmpty())
            return;
        N2oSetFieldSet newFieldSet = new N2oSetFieldSet();
        newFieldSet.setItems(pendingItems.toArray(FieldsetItem[]::new));
        fieldSets.add(newFieldSet);
        pendingItems.clear();
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetCol.class;
    }

    private FieldSetVisibilityScope initVisibilityScope(N2oFieldsetCol source, CompileProcessor p) {
        FieldSetVisibilityScope scope = new FieldSetVisibilityScope(p.getScope(FieldSetVisibilityScope.class));
        if (source.getVisible() != null && !Objects.equals(source.getVisible(), "true")) {
            String value = p.resolveJS(source.getVisible());
            if (StringUtils.isJs(value))
                scope.add(StringUtils.unwrapJs(value));
            else
                scope.add(value);
        }
        return scope;
    }
}

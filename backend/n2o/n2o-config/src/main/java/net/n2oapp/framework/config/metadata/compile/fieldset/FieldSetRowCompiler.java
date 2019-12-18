package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Компиляция строки филдсета
 */
@Component
public class FieldSetRowCompiler implements BaseSourceCompiler<FieldSet.Row, N2oFieldsetRow, CompileContext<?,?>> {

    @Override
    public FieldSet.Row compile(N2oFieldsetRow source, CompileContext<?,?> context, CompileProcessor p) {
        FieldSet.Row row = new FieldSet.Row();
        row.setClassName(source.getCssClass());
        row.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        if (source.getItems() != null) {
            List<FieldSet.Column> columns = new ArrayList<>();
            for(int i =0; i < source.getItems().length; i++) {
                if (source.getItems()[i] instanceof N2oFieldsetColumn) {
                    columns.add(p.compile(source.getItems()[i], context));
                } else {
                    N2oFieldsetColumn newCol = new N2oFieldsetColumn();
                    NamespaceUriAware[] items = new NamespaceUriAware[1];
                    items[0] = source.getItems()[i];
                    newCol.setItems(items);
                    columns.add(p.compile(newCol, context));
                }
            }
            row.setCols(columns);
        }
        return row;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetRow.class;
    }
}

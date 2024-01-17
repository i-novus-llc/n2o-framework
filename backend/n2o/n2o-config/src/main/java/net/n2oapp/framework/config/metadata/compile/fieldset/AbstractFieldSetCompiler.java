package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;

/**
 * Компиляции абстрактного филдсета
 */
public abstract class AbstractFieldSetCompiler<D extends FieldSet, S extends N2oFieldSet>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void compileFieldSet(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p, Object... scopes) {
        compiled.setLabel(p.resolveJS(source.getLabel()));
        compiled.setDescription(source.getDescription());
        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setProperties(p.mapAndResolveAttributes(source));
        compiled.setHelp(p.resolveJS(source.getHelp()));

        if (source.getFieldLabelLocation() != null) {
            compiled.setLabelPosition(FieldSet.LabelPosition.map(source.getFieldLabelLocation(), source.getFieldLabelAlign()));
        }
        if (source.getFieldLabelAlign() != null) {
            compiled.setLabelAlignment(FieldSet.LabelAlignment.map(source.getFieldLabelAlign()));
        }
        compiled.setLabelWidth(prepareSizeAttribute(source.getFieldLabelWidth()));

        compiled.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        compiled.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));

        if (source.getDependsOn() != null) {
            ControlDependency[] dependency = new ControlDependency[1];
            ControlDependency dep = new ControlDependency();
            dep.setType(ValidationType.reRender);
            List<String> ons = Arrays.asList(source.getDependsOn());
            dep.setOn(ons);
            dependency[0] = dep;
            compiled.setDependency(dependency);
        }

        compileContent(compiled, source, context, p, scopes);
    }

    private void compileContent(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p, Object... scopes) {
        if (source.getItems() == null)
            return;
        FieldSetVisibilityScope visibilityScope = initVisibilityScope(source, p);
        List<FieldSet.Row> rows = new ArrayList<>();
        for (FieldsetItem item : source.getItems()) {
            if (item instanceof N2oFieldsetRow) {
                rows.add(p.compile(item, context, visibilityScope, scopes));
            } else {
                N2oFieldsetRow newRow = new N2oFieldsetRow();
                newRow.setItems(new FieldsetItem[]{item});
                rows.add(p.compile(newRow, context, visibilityScope, scopes));
            }
        }
        compiled.setRows(rows);
    }

    private FieldSetVisibilityScope initVisibilityScope(S source, CompileProcessor p) {
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

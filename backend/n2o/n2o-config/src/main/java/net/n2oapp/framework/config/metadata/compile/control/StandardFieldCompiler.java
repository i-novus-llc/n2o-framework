package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class StandardFieldCompiler<D extends Control, S extends N2oStandardField> extends FieldCompiler<StandardField<D>, S> {

    protected StandardField<D> compileStandardField(D control, S source, CompileContext<?, ?> context, CompileProcessor p) {
        StandardField<D> field = constructField();
        compileField(field, source, context, p);
        compileDefaultValues(field, source, p);
        field.setSrc(p.cast(source.getFieldSrc(), p.resolve(property("n2o.api.field.src"), String.class)));
        field.setLabel(initLabel(source, p));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        if (source.getDescription() != null) {
            field.setDescription(p.resolveJS(source.getDescription().trim()));
        }
        if (source.getSrc() != null)
            control.setControlSrc(source.getSrc());
        control.setClassName(p.resolveJS(source.getCssClass()));
        control.setId(field.getId());
        field.setControl(control);
        return field;
    }

    private void compileDefaultValues(Field field, S source, CompileProcessor p) {
        ModelsScope defaultValues = p.getScope(ModelsScope.class);
        if (defaultValues != null && defaultValues.hasModels()) {
            Object defValue = null;
            if (source.getDefaultValue() != null) {
                defValue = p.resolve(source.getDefaultValue(), source.getDomain());
            } else {
                defValue = compileDefValues(source, p);
            }
            if (defValue != null) {
                if (StringUtils.isJs(defValue)) {
                    ModelLink defaultValue = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId(), null, field.getId());
                    defaultValue.setValue(defValue);
                    defaultValues.add(field.getId(), defaultValue);
                } else {
                    SubModelQuery subModelQuery = initSubModelQuery(field.getId(), p);
                    defaultValues.add(field.getId(), new ModelLink(defValue, subModelQuery));
                }
            }
        }
    }

    protected Object compileDefValues(S source, CompileProcessor p) {
        return null;
    }

    private String initLabel(S source, CompileProcessor p) {
        String label = p.resolveJS(source.getLabel());
        FieldSetScope scope = p.getScope(FieldSetScope.class);
        if (label == null && scope != null) {
            label = scope.get(source.getId());
        }
        if (label == null)
            label = source.getId();
        return label;
    }

    protected StandardField<D> constructField() {
        return new StandardField<>();
    }
}

package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
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
        StandardField<D> field = new StandardField<>();
        compileField(field, source, context, p);

        compileControl(control, source, p, field);
        field.setControl(control);
        return field;
    }

    private void compileDefaultValues(Field field, S source, CompileProcessor p) {
        UploadScope uploadScope = p.getScope(UploadScope.class);
        if (uploadScope != null && !UploadType.defaults.equals(uploadScope.getUpload()))
            return;
        ModelsScope defaultValues = p.getScope(ModelsScope.class);
        if (defaultValues != null && defaultValues.hasModels()) {
            Object defValue;
            if (source.getDefaultValue() != null) {
                defValue = p.resolve(source.getDefaultValue(), source.getDomain());
            } else {
                defValue = compileDefValues(source, p);
            }
            if (defValue != null) {
                if (StringUtils.isJs(defValue)) {
                    ModelLink defaultValue = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId());
                    defaultValue.setValue(defValue);
                    defaultValues.add(field.getId(), defaultValue);
                } else {
                    SubModelQuery subModelQuery = findSubModelQuery(field.getId(), p);
                    ModelLink modelLink = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId(), field.getId());
                    modelLink.setValue(defValue);
                    modelLink.setSubModelQuery(subModelQuery);
                    defaultValues.add(field.getId(), modelLink);
                }
            }
        }
    }

    protected Object compileDefValues(S source, CompileProcessor p) {
        return null;
    }

    /**
     * Настройка React компонента ввода по умолчанию
     */
    protected abstract String getControlSrcProperty();
}

package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.UploadScope;

import java.util.Map;

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

    protected void compileControl(D control, S source, CompileProcessor p, StandardField<D> field) {
        control.setSrc(p.cast(control.getSrc(), source.getControlSrc(), p.resolve(Placeholders.property(getControlSrcProperty()), String.class)));
        if (control.getSrc() == null)
            throw new N2oException("control src is required");
        control.setId(field.getId());
        control.setClassName(p.resolveJS(source.getCssClass()));
        compileDefaultValues(control, source, p);
    }

    /**
     * Настройка React компонента ввода по умолчанию
     */
    protected abstract String getControlSrcProperty();

    /**
     * Сборка значения по умолчанию у поля
     *
     * @param source Исходная модель поля
     * @param p      Процессор сборки
     * @return Значение по умолчанию поля
     */
    protected Object compileDefValues(S source, CompileProcessor p) {
        return null;
    }

    private void compileDefaultValues(D control, S source, CompileProcessor p) {
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
                if (defValue instanceof String) {
                    defValue = ScriptProcessor.resolveExpression((String) defValue);
                }
                if (StringUtils.isJs(defValue)) {
                    ModelLink defaultValue = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId());
                    defaultValue.setValue(defValue);
                    defaultValues.add(control.getId(), defaultValue);
                } else {
                    SubModelQuery subModelQuery = findSubModelQuery(control.getId(), p);
                    ModelLink modelLink = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId(), control.getId());
                    if (defValue instanceof DefaultValues) {
                        DefaultValues defaultValue = (DefaultValues) defValue;
                        Map<String, Object> values = defaultValue.getValues();
                        if (defaultValue.getValues() != null) {
                            for (String param : values.keySet()) {
                                if (values.get(param) instanceof String) {
                                    Object value = ScriptProcessor.resolveExpression((String) values.get(param));
                                    if (value != null)
                                        values.put(param, value);
                                }
                            }
                        }
                    }
                    modelLink.setValue(defValue);
                    modelLink.setSubModelQuery(subModelQuery);
                    defaultValues.add(control.getId(), modelLink);
                }
            }
        }
    }

}

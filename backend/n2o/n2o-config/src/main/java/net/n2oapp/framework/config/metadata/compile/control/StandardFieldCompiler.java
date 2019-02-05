package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
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
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.UploadScope;

import java.util.Map;

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
        if (source.getNoLabel() == null || !source.getNoLabel()) {
            field.setLabel(initLabel(source, p));
            field.setLabelClass(p.resolveJS(source.getLabelClass()));
        }
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
                    defaultValues.add(field.getId(), defaultValue);
                } else {
                    SubModelQuery subModelQuery = findSubModelQuery(field.getId(), p);
                    ModelLink modelLink = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId(), field.getId());
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
                    defaultValues.add(field.getId(), modelLink);
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

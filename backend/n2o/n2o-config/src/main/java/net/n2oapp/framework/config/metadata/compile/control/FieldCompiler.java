package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;

import java.util.*;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class FieldCompiler<D extends Field, S extends N2oField> extends ComponentCompiler<D, S> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.src";
    }

    protected void compileField(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        compileComponent(field, source, context, p);

        field.setId(source.getId());

        field.setVisible(source.getVisible());
        field.setEnabled(source.getEnabled());

        field.setLabel(initLabel(source, p));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        field.setDescription(p.resolveJS(source.getDescription()));
        field.setClassName(p.resolveJS(source.getCssClass()));
        compileDependencies(field, source, p);
    }

    protected String initLabel(S source, CompileProcessor p) {
        if (source.getNoLabel() == null || !source.getNoLabel()) {
            return p.resolveJS(source.getLabel());
        }
        return null;
    }


    /**
     * Компиляция зависимостей между полями
     *
     * @param field  клиентская модель элемента ввода
     * @param source исходная модель поля
     */
    protected void compileDependencies(Field field, S source, CompileProcessor p) {

        if (source.getDependencies() != null) {
            for (N2oField.Dependency d : source.getDependencies()) {
                ControlDependency dependency = new ControlDependency();
                if (d instanceof N2oField.EnablingDependency)
                    dependency.setType(ValidationType.enabled);
                else if (d instanceof N2oField.RequiringDependency)
                    dependency.setType(ValidationType.required);
                else if (d instanceof N2oField.VisibilityDependency)
                    dependency.setType(ValidationType.visible);
                else if (d instanceof N2oField.SetValueDependency)
                    dependency.setType(ValidationType.setValue);

                dependency.setExpression(ScriptProcessor.resolveFunction(d.getValue()));
                dependency.setApplyOnInit(p.cast(d.getApplyOnInit(), true));
                if (d.getOn() != null) {
                    List<String> ons = Arrays.asList(d.getOn());
                    ons.replaceAll(String::trim);
                    dependency.getOn().addAll(ons);
                }

                field.addDependency(dependency);
            }
        }

        if (source.getDependsOn() != null) {
            ControlDependency dependency = new ControlDependency();
            List<String> ons = Arrays.asList(source.getDependsOn());
            ons.replaceAll(String::trim);
            dependency.setOn(ons);
            dependency.setType(ValidationType.reRender);
            field.addDependency(dependency);
        }
    }
}

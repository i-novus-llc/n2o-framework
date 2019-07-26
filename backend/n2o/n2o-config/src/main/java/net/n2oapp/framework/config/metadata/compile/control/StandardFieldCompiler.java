package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class StandardFieldCompiler<D extends Control, S extends N2oStandardField> extends FieldCompiler<StandardField<D>, S> {

    protected StandardField<D> compileStandardField(D control, S source, CompileContext<?, ?> context, CompileProcessor p) {
        StandardField<D> field = new StandardField<>();
        compileField(field, source, context, p);
        field.setClassName(null);//для StandardField className должен попасть в control, а не field
        initValidations(source, field, context, p);
        compileFilters(source, p);
        compileCopied(source, p);
        compileControl(control, source, p, field);
        field.setControl(control);
        return field;
    }

    protected void compileControl(D control, S source, CompileProcessor p, StandardField<D> field) {
        control.setSrc(p.cast(control.getSrc(), source.getControlSrc(), p.resolve(Placeholders.property(getControlSrcProperty()), String.class)));
        if (control.getSrc() == null)
            throw new N2oException("control src is required");
        control.setId(source.getId());
        control.setClassName(p.resolveJS(source.getCssClass()));
        compileDefaultValues(null, control.getId(), source, p);
    }

    @Override
    protected String initLabel(S source, CompileProcessor p) {
        if (source.getNoLabel() == null || !source.getNoLabel()) {
            String label = super.initLabel(source, p);

            FieldSetScope scope = p.getScope(FieldSetScope.class);
            if (label == null && scope != null) {
                label = scope.get(source.getId());
            }
            if (label == null)
                label = source.getId();
            return label;
        } else
            return null;
    }

    /**
     * Настройка React компонента ввода по умолчанию
     */
    protected abstract String getControlSrcProperty();

}

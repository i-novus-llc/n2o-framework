package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oRangeField;
import net.n2oapp.framework.api.metadata.meta.control.BaseIntervalField;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import org.springframework.stereotype.Component;

@Component
public class IntervalFieldCompiler<C extends Control, S extends N2oRangeField> extends FieldCompiler<BaseIntervalField<C>, S> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRangeField.class;
    }

    protected String getControlSrcProperty() {
        return "n2o.api.control.input.interval.field.src";
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.input.interval.field.src";
    }

    @Override
    public BaseIntervalField<C> compile(S source, CompileContext<?, ?> context, CompileProcessor p) {

        BaseIntervalField<C> field = new BaseIntervalField<>();
        compileField(field, source, context, p);
        field.setClassName(null);//для IntervalField className должен попасть в control, а не field
        initValidations(source, field, context, p);
        compileFilters(source, p);
        compileCopied(source, p);
        compileControls(field, source, p, context);
        return field;
    }

    private void compileControls(BaseIntervalField<C> field, S source, CompileProcessor p,
                                 CompileContext<?, ?> context) {

        ModelsScope scope = p.getScope(ModelsScope.class);

        Object defValue = p.resolve(source.getBeginControl().getDefaultValue(), source.getBeginControl().getDomain());
        compileDefaultValues(defValue, source.getBeginControl().getId() + ".begin", source, p);
        source.getBeginControl().setDefaultValue(null);

        defValue = p.resolve(source.getEndControl().getDefaultValue(), source.getEndControl().getDomain());
        compileDefaultValues(defValue, source.getEndControl().getId() + ".end", source, p);
        source.getEndControl().setDefaultValue(null);

        StandardField beginConrol = p.compile(source.getBeginControl(), context);
        StandardField endControl = p.compile(source.getEndControl(), context);

        field.setBeginControl((C) beginConrol.getControl());
        field.setEndControl((C) endControl.getControl());
        beginConrol.getDependencies().forEach(f -> field.addDependency(f));
        endControl.getDependencies().forEach(f -> field.addDependency(f));
    }
}

package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.plain.N2oIntervalField;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.IntervalField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IntervalFieldCompiler<C extends Control, S extends N2oIntervalField> extends FieldCompiler<IntervalField<C>, S> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oIntervalField.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.input.interval.field.src";
    }

    @Override
    public IntervalField<C> compile(S source, CompileContext<?, ?> context, CompileProcessor p) {

        IntervalField<C> field = new IntervalField<>();
        compileField(field, source, context, p);
        field.setClassName(null);//для IntervalField className должен попасть в control, а не field
        initValidations(source, field, context, p);
        compileFilters(source, p);
        compileCopied(source, p);
        field.setBeginControl(compileControl(field, p, context, source.getBegin(), ".begin"));
        field.setEndControl(compileControl(field, p, context, source.getEnd(), ".end"));
        return field;
    }

    private C compileControl(IntervalField<C> owner, CompileProcessor p,
                                            CompileContext<?, ?> context, N2oField subField, String postId) {
        String fieldId = subField.getId();
        subField.setId(owner.getId() + postId);
        StandardField<C> standardField = p.compile(subField, context);
        standardField.getControl().setId(fieldId);
        if (standardField.getDependencies() != null)
            standardField.getDependencies().forEach(owner::addDependency);
        return standardField.getControl();
    }
}

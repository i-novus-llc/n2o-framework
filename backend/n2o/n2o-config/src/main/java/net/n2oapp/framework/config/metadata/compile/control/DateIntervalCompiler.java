package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.metadata.meta.control.DateInterval;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class DateIntervalCompiler  extends StandardFieldCompiler<DateInterval, N2oDateInterval> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDateInterval.class;
    }

    @Override
    public StandardField<DateInterval> compile(N2oDateInterval source, CompileContext<?,?> context, CompileProcessor p) {
        DateInterval dateInterval = new DateInterval();
        dateInterval.setOutputFormat(p.resolve(property("n2o.format.date.client"), String.class));
        dateInterval.setDateFormat(source.getDateFormat());
        dateInterval.setControlSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.date.interval.src"), String.class)));
        return compileStandardField(dateInterval, source, context, p);
    }

    @Override
    protected Object compileDefValues(N2oDateInterval source, CompileProcessor p) {
        if (source.getBegin() == null && source.getEnd() == null)
            return null;
        DefaultValues values = new DefaultValues();
        values.setValues(new HashMap<>());
        if (source.getBegin() != null) {
            values.getValues().put("begin", source.getBegin());
        }
        if (source.getEnd() != null) {
            values.getValues().put("end", source.getEnd());
        }
        return values;
    }
}

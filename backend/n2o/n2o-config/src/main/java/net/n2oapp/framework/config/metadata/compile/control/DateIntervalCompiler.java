package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.metadata.domain.Domain;
import net.n2oapp.framework.api.metadata.meta.control.DateInterval;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class DateIntervalCompiler extends IntervalFieldCompiler<DateInterval, N2oDateInterval> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDateInterval.class;
    }

    @Override
    public StandardField<DateInterval> compile(N2oDateInterval source, CompileContext<?, ?> context, CompileProcessor p) {
        DateInterval dateInterval = new DateInterval();
        source.setDomain(p.cast(source.getDomain(), p.resolve(property("n2o.api.control.date_interval.domain"), String.class)));
        Domain domain = Domain.getByName(source.getDomain());
        if (domain == null || domain.getJsFormat() == null)
            throw new IllegalStateException("Wrong domain for control " + source.getId());
        dateInterval.setOutputFormat(domain.getJsFormat());
        dateInterval.setDateFormat(p.cast(source.getDateFormat(), p.resolve(property("n2o.api.control.date_interval.format"), String.class)));
        dateInterval.setTimeFormat(source.getTimeFormat());
        dateInterval.setMin(source.getMin());
        dateInterval.setMax(source.getMax());
        dateInterval.setUtc(p.cast(source.getUtc(), p.resolve(property("n2o.api.control.date_interval.utc"), Boolean.class)));
        return compileStandardField(dateInterval, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.date_interval.src";
    }
}

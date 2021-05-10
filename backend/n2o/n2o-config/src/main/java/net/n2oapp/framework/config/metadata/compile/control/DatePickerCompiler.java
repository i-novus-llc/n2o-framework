package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
import net.n2oapp.framework.api.metadata.domain.Domain;
import net.n2oapp.framework.api.metadata.meta.control.DatePicker;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция календаря
 */
@Component
public class DatePickerCompiler extends StandardFieldCompiler<DatePicker, N2oDatePicker> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDatePicker.class;
    }

    @Override
    public StandardField<DatePicker> compile(N2oDatePicker source, CompileContext<?,?> context, CompileProcessor p) {
        DatePicker datePicker = new DatePicker();
        source.setDomain(p.cast(source.getDomain(), p.resolve(property("n2o.api.control.datetime.domain"), String.class)));
        Domain domain = Domain.getByName(source.getDomain());
        if (domain == null || domain.getJsFormat() == null)
            throw new IllegalStateException("Wrong domain for control " + source.getId());
        datePicker.setOutputFormat(domain.getJsFormat());
        datePicker.setDateFormat(p.cast(source.getDateFormat(), p.resolve(property("n2o.api.control.datetime.format"), String.class)));
        datePicker.setTimeFormat(source.getTimeFormat());
        datePicker.setMin(p.resolveJS(source.getMin()));
        datePicker.setMax(p.resolveJS(source.getMax()));
        datePicker.setPlaceholder(source.getPlaceholder());
        datePicker.setUtc(p.cast(source.getUtc(), p.resolve(property("n2o.api.control.datetime.utc"), Boolean.class)));
        return compileStandardField(datePicker, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.datetime.src";
    }
}

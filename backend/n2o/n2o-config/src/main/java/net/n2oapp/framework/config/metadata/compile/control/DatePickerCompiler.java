package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
import net.n2oapp.framework.api.metadata.domain.DomainEnum;
import net.n2oapp.framework.api.metadata.meta.control.DatePicker;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        source.setDomain(p.resolve(property("n2o.api.control.date_time.domain"), String.class));
        DomainEnum domain = DomainEnum.getByName(source.getDomain());
        if (domain == null || domain.getJsFormat() == null)
            throw new N2oException("Несуществующее значение атрибута 'domain' для поля '" + source.getId() + "'");
        datePicker.setOutputFormat(domain.getJsFormat());
        datePicker.setDateFormat(castDefault(source.getDateFormat(), () -> p.resolve(property("n2o.api.control.date_time.date_format"), String.class)));
        datePicker.setTimeFormat(source.getTimeFormat());
        datePicker.setMin(p.resolveJS(source.getMin()));
        datePicker.setMax(p.resolveJS(source.getMax()));
        datePicker.setPlaceholder(source.getPlaceholder());
        datePicker.setUtc(castDefault(source.getUtc(), () -> p.resolve(property("n2o.api.control.date_time.utc"), Boolean.class)));
        datePicker.setAutocomplete(castDefault(source.getAutocomplete(), () -> p.resolve(property("n2o.api.control.date_time.autocomplete"), String.class)));
        return compileStandardField(datePicker, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.date_time.src";
    }
}

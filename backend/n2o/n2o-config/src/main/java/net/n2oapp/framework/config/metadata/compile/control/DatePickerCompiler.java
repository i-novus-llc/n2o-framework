package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
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
        datePicker.setOutputFormat(p.resolve(property("n2o.format.date.client"), String.class));
        datePicker.setMin(source.getMin());
        datePicker.setMax(source.getMax());
        datePicker.setDateFormat(p.cast(source.getDateFormat(), p.resolve(property("n2o.api.control.datetime.format"), String.class)));
        datePicker.setControlSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.date.picker.src"), String.class)));
        return compileStandardField(datePicker, source, context, p);
    }
}

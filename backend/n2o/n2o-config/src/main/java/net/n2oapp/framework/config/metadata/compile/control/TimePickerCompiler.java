package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oTimePicker;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.TimePicker;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Сборка компонента ввода времени
 */
@Component
public class TimePickerCompiler extends StandardFieldCompiler<TimePicker, N2oTimePicker> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTimePicker.class;
    }

    @Override
    public StandardField<TimePicker> compile(N2oTimePicker source, CompileContext<?, ?> context, CompileProcessor p) {
        TimePicker timePicker = new TimePicker();
        timePicker.setPrefix(source.getPrefix());
        timePicker.setMode(p.cast(source.getMode(),
                p.resolve(property("n2o.api.control.time_picker.mode"), String.class).split(",")));
        timePicker.setTimeFormat(p.cast(source.getTimeFormat(),
                p.resolve(property("n2o.api.control.time_picker.time_format"), String.class)));
        timePicker.setFormat(p.cast(source.getFormat(),
                p.resolve(property("n2o.api.control.time_picker.format"), String.class)));
        return compileStandardField(timePicker, source, context, p);
    }


    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.time_picker.src";
    }
}

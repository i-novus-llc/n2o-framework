package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oTimePicker;
import net.n2oapp.framework.api.metadata.meta.control.TimePicker;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Сборка компонента ввода времени
 */
@Component
public class TimePickerCompiler extends ComponentCompiler<TimePicker, N2oTimePicker> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTimePicker.class;
    }

    @Override
    public TimePicker compile(N2oTimePicker source, CompileContext<?, ?> context, CompileProcessor p) {
        TimePicker timePicker = new TimePicker();
        compileComponent(timePicker, source, context, p);

//        timePicker.setSrc(p.cast(source.getSrc(), p.resolve("", )""));
        timePicker.setId(source.getId());
        timePicker.setPrefix(source.getPrefix());
        timePicker.setMode(p.cast(source.getMode(), p.resolve(property("n2o.api.control.time-picker.mode"), String.class).split(",")));
        timePicker.setDataFormat(p.cast(source.getDataFormat(), p.resolve(property("n2o.api.control.time-picker.data-format"), String.class)));
        timePicker.setFormat(p.cast(source.getFormat(), p.resolve(property("n2o.api.control.time-picker.format"), String.class)));
        timePicker.setDefaultValue(source.getDefaultValue());
        return timePicker;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.time-picker.src";
    }
}

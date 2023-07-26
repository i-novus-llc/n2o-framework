package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oNumberPicker;
import net.n2oapp.framework.api.metadata.meta.control.NumberPicker;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента выбора числа из диапазона
 */
@Component
public class NumberPickerCompiler extends StandardFieldCompiler<NumberPicker, N2oNumberPicker> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oNumberPicker.class;
    }

    @Override
    public StandardField<NumberPicker> compile(N2oNumberPicker source, CompileContext<?, ?> context, CompileProcessor p) {
        NumberPicker numberPicker = new NumberPicker();
        numberPicker.setMin(p.cast(source.getMin(),
                () -> p.resolve(property("n2o.api.control.number_picker.min"), Integer.class)));
        numberPicker.setMax(p.cast(source.getMax(),
                () -> p.resolve(property("n2o.api.control.number_picker.max"), Integer.class)));
        numberPicker.setStep(p.cast(source.getStep(),
                () -> p.resolve(property("n2o.api.control.number_picker.step"), Integer.class)));
        source.setDomain("integer");
        return compileStandardField(numberPicker, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.number_picker.src";
    }
}

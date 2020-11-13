package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oNumberPicker;
import net.n2oapp.framework.api.metadata.meta.control.NumberPicker;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента выбора числа из диапазона
 */
@Component
public class NumberPickerCompiler extends FieldCompiler<NumberPicker, N2oNumberPicker> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oNumberPicker.class;
    }

    @Override
    public NumberPicker compile(N2oNumberPicker source, CompileContext<?, ?> context, CompileProcessor p) {
        NumberPicker numberPicker = new NumberPicker();
        compileField(numberPicker, source, context, p);
        numberPicker.setMin(source.getMin());
        numberPicker.setMax(source.getMax());
        numberPicker.setStep(p.cast(source.getStep(),
                p.resolve(property("n2o.api.control.number_picker.step"), Integer.class)));
        compileDefaultValues(numberPicker, source, p);
        return numberPicker;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.number_picker.src";
    }
}

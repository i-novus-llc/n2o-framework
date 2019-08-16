package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.list.N2oSlider;
import net.n2oapp.framework.api.metadata.meta.control.Slider;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

/**
 * Компиляция ползунка
 */
@Component
public class SliderCompiler extends ListControlCompiler<Slider, N2oSlider>{
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSlider.class;
    }

    @Override
    public StandardField<Slider> compile(N2oSlider source, CompileContext<?, ?> context, CompileProcessor p) {
        Slider slider = new Slider();
        slider.setMultiple(N2oSlider.Mode.range.equals(source.getMode()));
        boolean isVertical = source.isVertical();
        slider.setVertical(isVertical);

        slider.setShowTooltip(p.resolve(Placeholders.property("n2o.api.control.slider.tooltip"), Boolean.class));
        slider.setTooltipPlacement(isVertical ? "left" : "top");
        String measure = source.getMeasure();
        if (measure == null) {
            measure = "";
        }
        slider.setTooltipFormatter(Placeholders.js("${this}" + measure));

        slider.setMin(source.getMin());
        slider.setMax(source.getMax());
        slider.setStep(source.getStep());
        return compileListControl(slider, source, context, p);
    }
    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.slider.src";
    }
}

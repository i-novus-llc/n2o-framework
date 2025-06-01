package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.list.N2oSlider;
import net.n2oapp.framework.api.metadata.meta.control.Slider;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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

        N2oSlider.ModeEnum mode = castDefault(source.getMode(),
                () -> p.resolve(property("n2o.api.control.slider.mode"), N2oSlider.ModeEnum.class));
        slider.setMultiple(N2oSlider.ModeEnum.RANGE.equals(mode));
        boolean isVertical = Boolean.TRUE.equals((castDefault(source.getVertical(),
                () -> p.resolve(property("n2o.api.control.slider.vertical"), Boolean.class))));
        slider.setVertical(isVertical);

        slider.setShowTooltip(p.resolve(Placeholders.property("n2o.api.control.slider.tooltip"), Boolean.class));
        slider.setTooltipPlacement(isVertical ? "left" : "top");
        slider.setTooltipFormatter("${this}" + castDefault(source.getMeasure(), ""));

        slider.setMin(source.getMin());
        slider.setMax(source.getMax());
        slider.setStep(castDefault(source.getStep(),
                () -> p.resolve(Placeholders.property("n2o.api.control.slider.step"), Integer.class)));
        return compileListControl(slider, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.slider.src";
    }
}

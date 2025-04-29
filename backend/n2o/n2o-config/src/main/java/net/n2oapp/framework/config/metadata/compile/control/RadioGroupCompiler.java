package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.list.N2oRadioGroup;
import net.n2oapp.framework.api.metadata.control.list.RadioGroupTypeEnum;
import net.n2oapp.framework.api.metadata.meta.control.RadioGroup;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

@Component
public class RadioGroupCompiler extends ListControlCompiler<RadioGroup, N2oRadioGroup> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.radio_group.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRadioGroup.class;
    }

    @Override
    public StandardField<RadioGroup> compile(N2oRadioGroup source, CompileContext<?, ?> context, CompileProcessor p) {
        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setInline(castDefault(source.getInline(), source.getType() == RadioGroupTypeEnum.TABS ?
                () -> p.resolve(property("n2o.api.control.radio_group.tabs_inline"), Boolean.class) :
                () -> p.resolve(property("n2o.api.control.radio_group.inline"), Boolean.class)));
        radioGroup.setType(castDefault(source.getType(),
                () -> p.resolve(property("n2o.api.control.radio_group.type"), RadioGroupTypeEnum.class)));
        StandardField<RadioGroup> result = compileListControl(radioGroup, source, context, p);
        return compileFetchDependencies(result, source, p);
    }
}

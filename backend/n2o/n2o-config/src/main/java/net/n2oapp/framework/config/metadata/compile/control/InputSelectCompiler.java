package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.list.ListTypeEnum;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;


@Component
public class InputSelectCompiler extends ListControlCompiler<InputSelect, N2oInputSelect> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.input_select.src";
    }

    @Override
    public StandardField<InputSelect> compile(N2oInputSelect source, CompileContext<?,?> context, CompileProcessor p) {
        InputSelect inputSelect = new InputSelect();
        source.setSearch(castDefault(source.getSearch(), () -> source.getQueryId() != null));
        inputSelect.setResetOnBlur(castDefault(source.getResetOnBlur(),
                () -> p.resolve(property("n2o.api.control.input_select.reset_on_blur"), Boolean.class)));
        inputSelect.setDescriptionFieldId(source.getDescriptionFieldId());
        inputSelect.setThrottleDelay(castDefault(source.getThrottleDelay(),
                () -> p.resolve(property("n2o.api.control.input_select.throttle_delay"), Integer.class)));
        inputSelect.setSearchMinLength(castDefault(source.getSearchMinLength(),
                () -> p.resolve(property("n2o.api.control.input_select.search_min_length"), Integer.class)));
        inputSelect.setMaxTagCount(source.getMaxTagCount());
        inputSelect.setMaxTagTextLength(castDefault(source.getMaxTagTextLength(),
                () -> p.resolve(property("n2o.api.control.input_select.max_tag_text_length"), Integer.class)));
        ListTypeEnum type = castDefault(source.getType(), ListTypeEnum.SINGLE);
        switch (type) {
            case CHECKBOXES:
                inputSelect.setClosePopupOnSelect(false);
                inputSelect.setHasCheckboxes(true);
                inputSelect.setMultiSelect(true);
                break;
            case MULTI:
                inputSelect.setClosePopupOnSelect(false);
                inputSelect.setHasCheckboxes(false);
                inputSelect.setMultiSelect(true);
                break;
            case SINGLE:
                inputSelect.setHasCheckboxes(false);
                inputSelect.setMultiSelect(false);
                break;
        }
        return compileListControl(inputSelect, source, context, p);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInputSelect.class;
    }
}

package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.list.SearchSideEnum;
import net.n2oapp.framework.api.metadata.control.plain.N2oAutoComplete;
import net.n2oapp.framework.api.metadata.meta.control.AutoComplete;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция компонента ввода текста с автоподбором
 */
@Component
public class AutoCompleteCompiler extends ListControlCompiler<AutoComplete, N2oAutoComplete> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.auto_complete.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAutoComplete.class;
    }

    @Override
    public StandardField<AutoComplete> compile(N2oAutoComplete source, CompileContext<?, ?> context, CompileProcessor p) {
        AutoComplete autoComplete = new AutoComplete();
        autoComplete.setResetOnBlur(castDefault(source.getResetOnBlur(),
                () -> p.resolve(property("n2o.api.control.auto_complete.reset_on_blur"), Boolean.class)));
        autoComplete.setValueFieldId(castDefault(source.getValueFieldId(),
                () -> p.resolve(property("n2o.api.control.auto_complete.value_field_id"), String.class)));
        autoComplete.setLabelFieldId(castDefault(source.getLabelFieldId(), autoComplete.getValueFieldId(), "name"));
        autoComplete.setInputLabelFieldId(castDefault(source.getInputLabelFieldId(), autoComplete.getLabelFieldId()));
        autoComplete.setTags(true);
        autoComplete.setMaxTagTextLength(castDefault(source.getMaxTagTextLength(),
                () -> p.resolve(property("n2o.api.control.auto_complete.max_tag_text_length"), Integer.class)));
        autoComplete.setSearchSide(castDefault(source.getSearchSide(),
                () -> p.resolve(property("n2o.api.control.auto_complete.search_side"), SearchSideEnum.class)));
        compileData(source, autoComplete, context, p);

        return compileStandardField(autoComplete, source, context, p);
    }

    @Override
    protected String initQuickSearchParam(AutoComplete listControl, N2oListField source, CompileProcessor p) {
        return castDefault(source.getSearchFilterId(), "name");
    }
}

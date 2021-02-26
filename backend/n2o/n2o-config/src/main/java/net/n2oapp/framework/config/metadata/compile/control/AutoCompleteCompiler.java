package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oAutoComplete;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.control.AutoComplete;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.util.FieldCompileUtil;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента ввода текста с автоподбором
 */
@Component
public class AutoCompleteCompiler extends StandardFieldCompiler<AutoComplete, N2oAutoComplete> {

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
        autoComplete.setPlaceholder(p.resolveJS(source.getPlaceholder()));
        autoComplete.setValueFieldId(p.cast(source.getValueFieldId(), "name"));
        autoComplete.setTags(p.cast(source.getTags(),
                p.resolve(property("n2o.api.control.auto_complete.tags"), Boolean.class)));
        autoComplete.setMaxTagTextLength(p.cast(source.getMaxTagTextLength(),
                p.resolve(property("n2o.api.control.input.select.max_tag_text_length"), Integer.class)));
        if (source.getQueryId() != null)
            autoComplete.setDataProvider(compileDataProvider(source, context, p));
        else if (source.getOptions() != null) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Map<String, String> option : source.getOptions()) {
                DataSet dataItem = new DataSet();
                option.forEach((f, v) -> dataItem.put(f, p.resolve(v)));
                list.add(dataItem);
            }
            autoComplete.setData(list);
        }
        return compileStandardField(autoComplete, source, context, p);
    }

    private ClientDataProvider compileDataProvider(N2oAutoComplete source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oClientDataProvider dataProvider = N2oClientDataProviderUtil.initFromField(source.getPreFilters(), source.getQueryId(), p);
        dataProvider.setQuickSearchParam(p.cast(source.getSearchFilterId(), "name"));
        source.addDependencies(FieldCompileUtil.getResetOnChangeDependency(source));
        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }
}

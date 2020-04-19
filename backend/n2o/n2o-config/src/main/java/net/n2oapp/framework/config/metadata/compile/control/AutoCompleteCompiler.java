package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oAutoComplete;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.control.AutoComplete;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Компиляция поля для ввода текста c автоподбором
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
        autoComplete.setValueFieldId(p.cast(source.getValueFieldId(), "name"));
        if (source.getQueryId() != null)
            autoComplete.setDataProvider(initDataProvider(source, context, p));
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

    private ClientDataProvider initDataProvider(N2oAutoComplete source, CompileContext<?, ?> context, CompileProcessor p) {
        QueryContext queryContext = new QueryContext(source.getQueryId());
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        queryContext.setFailAlertWidgetId(modelsScope != null ? modelsScope.getWidgetId() : null);
        CompiledQuery query = p.getCompiled(queryContext);
        String route = query.getRoute();
        p.addRoute(new QueryContext(source.getQueryId(), route));
        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setUrl(route);
        dataProvider.setQuickSearchParam(p.cast(source.getSearchFilterId(), "name"));
        return p.compile(dataProvider, context, p);
    }
}


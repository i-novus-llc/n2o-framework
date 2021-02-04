package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.plain.N2oAutoComplete;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.control.AutoComplete;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
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
        autoComplete.setMaxTagTextLength(p.cast(source.getMaxTagTextLength() ,
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
        QueryContext queryContext = new QueryContext(source.getQueryId());
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        queryContext.setFailAlertWidgetId(modelsScope != null ? modelsScope.getWidgetId() : null);
        CompiledQuery query = p.getCompiled(queryContext);
        String route = query.getRoute();
        p.addRoute(new QueryContext(source.getQueryId(), route));
        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setUrl(route);
        dataProvider.setQuickSearchParam(p.cast(source.getSearchFilterId(), "name"));

        N2oPreFilter[] preFilters = source.getPreFilters();
        if (preFilters != null) {
            N2oParam[] queryParams = new N2oParam[preFilters.length];
            for (int i = 0; i < preFilters.length; i++) {
                N2oPreFilter preFilter = preFilters[i];
                N2oQuery.Filter filter = query.getFilterByPreFilter(preFilter);
                N2oParam queryParam = new N2oParam();
                queryParam.setName(query.getFilterIdToParamMap().get(filter.getFilterField()));
                if (preFilter.getParam() == null) {
                    queryParam.setValueList(getPrefilterValue(preFilter));
                    queryParam.setRefModel(preFilter.getRefModel());
                    queryParam.setRefWidgetId(preFilter.getRefWidgetId());
                } else {
                    queryParam.setValueParam(preFilter.getParam());
                }
                queryParams[i] = queryParam;

                if (Boolean.TRUE.equals(preFilter.getResetOnChange())
                        && StringUtils.isLink(preFilter.getValue())) {
                    N2oField.ResetDependency reset = new N2oField.ResetDependency();
                    reset.setOn(new String[]{preFilter.getValue().substring(1, preFilter.getValue().length() - 1)});
                    source.addDependency(reset);
                }
            }
            dataProvider.setQueryParams(queryParams);
        }

        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}

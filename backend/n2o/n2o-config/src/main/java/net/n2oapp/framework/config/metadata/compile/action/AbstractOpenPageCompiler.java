package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.CompileUtil;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Абстрактная реализация компиляция open-page, show-modal
 */
@Component
public abstract class AbstractOpenPageCompiler<D extends AbstractAction, S extends N2oAbstractPageAction> extends AbstractActionCompiler<D, S> {

    protected Map<String, ModelLink> initRouteInfos(List<N2oPreFilter> preFilters, Set<String> pathParams, CompileProcessor p) {
        if (preFilters == null) return null;
        CompiledQuery query = p.getScope(CompiledQuery.class);
        String queryId = query == null ? null : query.getId();
        Map<String, ModelLink> res = new HashMap<>();
        preFilters.stream().filter(n2oPreFilter -> n2oPreFilter.getParam() != null && !pathParams.contains(n2oPreFilter.getParam()))
                .forEach(preFilter -> res.put(preFilter.getParam(), createModelLink(preFilter, queryId, preFilter.getParam(), p)));
        return res;
    }

    protected List<N2oPreFilter> initPreFilters(N2oAbstractPageAction source, String masterIdParam, WidgetScope widgetScope, CompileProcessor p) {
        List<N2oPreFilter> preFilters = new ArrayList<>();
        ReduxModel model = ReduxModel.RESOLVE;
        String widgetId = widgetScope == null ? null : widgetScope.getWidgetId();
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                model = modelAware.getModel();
            }
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                widgetId = widgetIdAware.getWidgetId();
            }
        }
        if (source.getDetailFieldId() != null) {
            N2oPreFilter filter = new N2oPreFilter();
            filter.setFieldId(p.cast(source.getDetailFieldId(), N2oQuery.Field.PK));
            filter.setType(FilterType.eq);
            filter.setValueAttr(Placeholders.ref(p.cast(source.getMasterFieldId(), N2oQuery.Field.PK)));
            filter.setRefWidgetId(widgetId);
            if ((source.getMasterFieldId() == null || source.getMasterFieldId().equals(N2oQuery.Field.PK)) && ReduxModel.RESOLVE.equals(model)) {
                filter.setParam(p.cast(masterIdParam, filter.getFieldId()));
            } else {
                filter.setParam(filter.getFieldId());
            }
            filter.setRefModel(ReduxModel.RESOLVE);
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null) {
                filter.setRefPageId(pageScope.getPageId());
            }
            preFilters.add(filter);
        }
        if (source.getPreFilters() != null) {
            for (N2oPreFilter preFilter : source.getPreFilters()) {
                N2oPreFilter filter = new N2oPreFilter();
                filter.setFieldId(preFilter.getFieldId());
                filter.setParam(filter.getFieldId());
                filter.setType(preFilter.getType());
                filter.setValueAttr(preFilter.getValueAttr());
                filter.setValuesAttr(preFilter.getValuesAttr());
                filter.setRefWidgetId(p.cast(preFilter.getRefWidgetId(), widgetScope.getWidgetId()));
                filter.setRefModel(p.cast(preFilter.getRefModel(), model, ReduxModel.RESOLVE));
                PageScope pageScope = p.getScope(PageScope.class);
                if (pageScope != null) {
                    filter.setRefPageId(pageScope.getPageId());
                }
                preFilters.add(filter);
            }
        }
        return preFilters;
    }

    protected ModelLink createModelLink(N2oPreFilter preFilter, String queryId, String param,  CompileProcessor p) {
        Object prefilterValue = getPrefilterValue(preFilter);
        if (StringUtils.isJs(prefilterValue)) {
            ModelLink link = new ModelLink(preFilter.getRefModel(), CompileUtil.generateWidgetId(preFilter.getRefPageId(), preFilter.getRefWidgetId()), null, param, queryId);
            link.setValue(prefilterValue);
            return link;
        } else {
            return new ModelLink(prefilterValue);
        }
    }

    protected Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}

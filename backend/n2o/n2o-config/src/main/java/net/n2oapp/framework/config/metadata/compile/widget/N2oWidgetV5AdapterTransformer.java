package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oVisibilityDependency;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Трансформация widget
 * Перенос старых(widget-4.0) атрибутов в новые(widget-5.0)
 */
@Component
public class N2oWidgetV5AdapterTransformer implements SourceTransformer<N2oWidget>, SourceClassAware {

    public N2oWidget transform(N2oWidget source, SourceProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getQueryId() != null || source.getFetchOnInit() != null || source.getPreFilters() != null ||
                source.getObjectId() != null ||
                source.getUpload() != null || source.getDependsOn() != null || source.getDependencyCondition() != null) {
            if (source.getDatasource() == null)
                source.setDatasource(new N2oDatasource());
            source.getDatasource().setQueryId(source.getQueryId());
            source.getDatasource().setObjectId(source.getObjectId());
            source.getDatasource().setFilters(source.getPreFilters());
            source.getDatasource().setRoute(source.getRoute());
            if (source.getUpload() != null) {
                switch (source.getUpload()) {
                    case query:
                        source.getDatasource().setDefaultValuesMode(DefaultValuesMode.query);
                        break;
                    case copy:
                        source.getDatasource().setDefaultValuesMode(DefaultValuesMode.merge);
                        break;
                    case defaults:
                        source.getDatasource().setDefaultValuesMode(DefaultValuesMode.defaults);
                        source.getDatasource().setQueryId(source.getDefaultValuesQueryId());
                        break;
                    default:
                        source.getDatasource().setDefaultValuesMode(DefaultValuesMode.query);
                }
            }
            if (source.getDependsOn() != null) {
                if (pageScope == null || pageScope.getWidgetIdSourceDatasourceMap() == null)
                    throw new N2oException(String.format("There is a link to %s, but the context is not defined ", source.getDependsOn()));
                N2oDatasource.FetchDependency fetchDependency = new N2oDatasource.FetchDependency();
                String datasourceId = pageScope.getWidgetIdSourceDatasourceMap().get(source.getDependsOn());
                fetchDependency.setOn(datasourceId);
                fetchDependency.setModel(ReduxModel.RESOLVE);
                source.getDatasource().setDependencies(new N2oDatasource.Dependency[]{fetchDependency});
                //поддержка master-detail связи
                if (source.getDetailFieldId() != null) {
                    List<N2oPreFilter> preFilters = source.getDatasource().getFilters() == null ?
                            new ArrayList<>() :
                            new ArrayList<>(Arrays.asList(source.getDatasource().getFilters()));
                    String value = "{" + (source.getMasterFieldId() == null ? N2oQuery.Field.PK : source.getMasterFieldId()) + "}";
                    N2oPreFilter masterFilter = new N2oPreFilter(source.getDetailFieldId(), value, FilterType.eq);
                    String param = source.getMasterParam();
                    if (param == null && source.getRoute() != null) {
                        List<String> params = RouteUtil.getParams(source.getRoute());
                        if (!params.isEmpty()) {
                            if (params.size() > 1) {
                                throw new N2oException("Widget route can not contain more then one param: " + source.getRoute());
                            }
                            param = params.get(0);
                        }
                    }
                    masterFilter.setParam(param);
                    masterFilter.setModel(ReduxModel.RESOLVE);
                    masterFilter.setDatasource(datasourceId);
                    masterFilter.setRequired(true);
                    preFilters.add(masterFilter);
                    source.getDatasource().setFilters(preFilters.toArray(new N2oPreFilter[preFilters.size()]));
                }
            }
            source.getDatasource().setSize(source.getSize());
            if (source.getDependencyCondition() != null) {
                N2oVisibilityDependency visibilityDependency = new N2oVisibilityDependency();
                visibilityDependency.setValue(source.getDependencyCondition());
                if (source.getDependsOn() != null) {
                    if (pageScope == null || pageScope.getWidgetIdSourceDatasourceMap() == null)
                        throw new N2oException(String.format("There is a link to %s, but the context is not defined ", source.getDependsOn()));
                    visibilityDependency.setDatasource(pageScope.getWidgetIdSourceDatasourceMap().get(source.getDependsOn()));
                }
                visibilityDependency.setModel(ReduxModel.RESOLVE);
                source.setDependencies(new N2oDependency[]{visibilityDependency});
            }
        }
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oWidget.class;
    }
}

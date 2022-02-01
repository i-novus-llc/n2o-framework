package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.tools.N2oCounter;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oVisibilityDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Исходная модель виджета
 */
@Getter
@Setter
public abstract class N2oWidget extends N2oMetadata implements SourceComponent, ExtensionAttributesAware, PreFiltersAware {
    private String src;
    private String customize;
    private String name;
    @Deprecated
    private String route;
    @Deprecated
    private String queryId;
    @Deprecated
    private String defaultValuesQueryId;
    @Deprecated
    private String objectId;
    private String datasourceId;
    private N2oDatasource datasource;
    @Deprecated
    private Integer size;
    private String cssClass;
    private String style;
    private Boolean border;
    private String refId;
    @Deprecated
    private String masterParam;
    /**
     * Автоматическая установка фокуса на виджете
     */
    private Boolean autoFocus;
    @Deprecated
    private UploadType upload;
    @Deprecated
    private String dependsOn;
    @Deprecated
    private String dependencyCondition;
    private String result;
    private String icon;
    @Deprecated
    private String masterFieldId;
    @Deprecated
    private String detailFieldId;
    private String visible;
    private Boolean refreshDependentContainer;
    @Deprecated
    private N2oPreFilter[] preFilters;
    private N2oCounter counter;
    private RefreshPolity refreshPolity;
    private ActionsBar[] actions;
    private GenerateType actionGenerate;
    private N2oToolbar[] toolbars;
    private Map<N2oNamespace, Map<String, String>> extAttributes;
    private N2oDependency[] dependencies;

    public Class getWidgetClass() {
        return this.getClass();
    }

    @Override
    public final String getPostfix() {
        return "widget";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oWidget.class;
    }

    /**
     * Добавить предустановленные фильтры
     *
     * @param preFilters Список предустановленных фильтров
     */
    public void addPreFilters(List<N2oPreFilter> preFilters) {
        List<N2oPreFilter> list = new ArrayList<>();
        if (this.preFilters != null)
            list.addAll(Arrays.asList(this.preFilters));
        list.addAll(preFilters);
        this.preFilters = list.toArray(new N2oPreFilter[list.size()]);
    }

    @Deprecated
    public void adapterV4() {
        if (getQueryId() != null || getDefaultValuesQueryId() != null || getPreFilters() != null ||
                getObjectId() != null ||
                getUpload() != null || getDependsOn() != null || getDependencyCondition() != null) {
            N2oDatasource datasource = new N2oDatasource();
            setDatasource(datasource);
            datasource.setQueryId(getQueryId());
            datasource.setObjectId(getObjectId());
            datasource.setFilters(getPreFilters());
            datasource.setRoute(getRoute());

            if (getUpload() != null) {
                switch (getUpload()) {
                    case query:
                        datasource.setDefaultValuesMode(DefaultValuesMode.query);
                        break;
                    case copy:
                        datasource.setDefaultValuesMode(DefaultValuesMode.merge);
                        break;
                    case defaults:
                        datasource.setDefaultValuesMode(DefaultValuesMode.defaults);
                        datasource.setQueryId(getDefaultValuesQueryId());
                        break;
                    default:
                        datasource.setDefaultValuesMode(DefaultValuesMode.query);
                }
            }

            if (getDependsOn() != null) {
                N2oDatasource.FetchDependency fetchDependency = new N2oDatasource.FetchDependency();
                fetchDependency.setOn(getDependsOn());//не учитывается, что виджет может использовать datasource из 7.19
                fetchDependency.setModel(ReduxModel.resolve);
                datasource.setDependencies(new N2oDatasource.Dependency[]{fetchDependency});
                //поддержка master-detail связи
                if (getDetailFieldId() != null) {
                    List<N2oPreFilter> preFilters = datasource.getFilters() == null ?
                            new ArrayList<>() :
                            new ArrayList<>(Arrays.asList(datasource.getFilters()));
                    String value = Placeholders.ref(getMasterFieldId() == null ? N2oQuery.Field.PK : getMasterFieldId());
                    N2oPreFilter masterFilter = new N2oPreFilter(getDetailFieldId(), value, FilterType.eq);
                    String param = getMasterParam();
                    if (param == null && getRoute() != null && getRoute().contains(":")) {
                        param = getRoute().substring(getRoute().indexOf(":") + 1, getRoute().lastIndexOf("/"));
                    }
                    masterFilter.setParam(param);
                    masterFilter.setModel(ReduxModel.resolve);
                    masterFilter.setDatasource(getDependsOn());
                    masterFilter.setRequired(true);
                    preFilters.add(masterFilter);
                    datasource.setFilters(preFilters.toArray(new N2oPreFilter[0]));
                }
                if (datasource.getFilters() != null) {
                    for (N2oPreFilter filter : datasource.getFilters())
                        filter.setDatasource(getDependsOn());
                }
            }
            datasource.setSize(getSize());
            if (getDependencyCondition() != null || getVisible() != null) {
                N2oVisibilityDependency visibilityDependency = new N2oVisibilityDependency();
                if (getDependencyCondition() == null)
                    visibilityDependency.setValue(StringUtils.unwrapLink(getVisible()));
                else
                    visibilityDependency.setValue(getDependencyCondition());
                if (getDependsOn() != null ) {
                    visibilityDependency.setDatasource(getDependsOn());//не учитывается, что виджет может использовать datasource из 7.19
                }
                visibilityDependency.setModel(ReduxModel.resolve);
                setDependencies(new N2oDependency[]{visibilityDependency});
            }
        }
    }
}

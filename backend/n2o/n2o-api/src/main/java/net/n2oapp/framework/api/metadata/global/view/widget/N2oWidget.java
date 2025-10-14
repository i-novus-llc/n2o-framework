package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.RegionItem;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.*;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oVisibilityDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Исходная модель виджета
 */
@Getter
@Setter
public abstract class N2oWidget extends N2oMetadata
        implements SourceComponent, ExtensionAttributesAware, PreFiltersAware, RegionItem,
        DatasourceIdAware, ActionBarAware, ToolbarsAware {

    private String src;
    @Deprecated
    private String route;
    @Deprecated
    private String queryId;
    @Deprecated
    private String defaultValuesQueryId;
    @Deprecated
    private String objectId;
    private String datasourceId;
    private N2oStandardDatasource datasource;
    private Boolean fetchOnInit;
    private Boolean fetchOnVisibility;
    @Deprecated
    private Integer size;
    private String cssClass;
    private String style;
    @Deprecated
    private String masterParam;
    /**
     * Автоматическая установка фокуса на виджете
     */
    private Boolean autoFocus;
    @Deprecated
    private DefaultValuesModeEnum upload;
    @Deprecated
    private String dependsOn;
    @Deprecated
    private String masterFieldId;
    @Deprecated
    private String detailFieldId;
    private String visible;
    @Deprecated
    private N2oPreFilter[] preFilters;
    private ActionBar[] actions;
    private N2oToolbar[] toolbars;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
    private N2oDependency[] dependencies;

    @Override
    public final String getPostfix() {
        return "widget";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oWidget.class;
    }

    @Deprecated
    public void adapterV4() {
        if (getQueryId() != null || getDefaultValuesQueryId() != null || getPreFilters() != null ||
                getObjectId() != null ||
                this.getUpload() != null || getDependsOn() != null) {
            N2oStandardDatasource standardDatasource = new N2oStandardDatasource();
            setDatasource(standardDatasource);
            standardDatasource.setQueryId(getQueryId());
            standardDatasource.setObjectId(getObjectId());
            standardDatasource.setFilters(getPreFilters());
            standardDatasource.setRoute(getRoute());

            if (this.getUpload() != null) {
                switch (this.getUpload()) {
                    case QUERY:
                        standardDatasource.setDefaultValuesMode(DefaultValuesModeEnum.QUERY);
                        break;
                    case MERGE:
                        standardDatasource.setDefaultValuesMode(DefaultValuesModeEnum.MERGE);
                        break;
                    case DEFAULTS:
                        standardDatasource.setDefaultValuesMode(DefaultValuesModeEnum.DEFAULTS);
                        standardDatasource.setQueryId(getDefaultValuesQueryId());
                        break;
                    default:
                        standardDatasource.setDefaultValuesMode(DefaultValuesModeEnum.QUERY);
                }
            }

            if (getDependsOn() != null) {
                N2oDatasource.FetchDependency fetchDependency = new N2oStandardDatasource.FetchDependency();
                fetchDependency.setOn(getDependsOn());//не учитывается, что виджет может использовать datasource из 7.19
                fetchDependency.setModel(ReduxModelEnum.RESOLVE);
                standardDatasource.setDependencies(new N2oDatasource.Dependency[]{fetchDependency});
                //поддержка master-detail связи
                if (getDetailFieldId() != null) {
                    List<N2oPreFilter> preFilterList = standardDatasource.getFilters() == null ?
                            new ArrayList<>() :
                            new ArrayList<>(Arrays.asList(standardDatasource.getFilters()));
                    String value = Placeholders.ref(getMasterFieldId() == null ? QuerySimpleField.PK : getMasterFieldId());
                    N2oPreFilter masterFilter = new N2oPreFilter(getDetailFieldId(), value, FilterTypeEnum.EQ);
                    String param = getMasterParam();
                    if (param == null && getRoute() != null && getRoute().contains(":")) {
                        param = getRoute().substring(getRoute().indexOf(":") + 1, getRoute().lastIndexOf("/"));
                    }
                    masterFilter.setParam(param);
                    masterFilter.setModel(ReduxModelEnum.RESOLVE);
                    masterFilter.setDatasourceId(getDependsOn());
                    masterFilter.setRequired(true);
                    preFilterList.add(masterFilter);
                    standardDatasource.setFilters(preFilterList.toArray(new N2oPreFilter[0]));
                }
                if (standardDatasource.getFilters() != null) {
                    for (N2oPreFilter filter : standardDatasource.getFilters())
                        filter.setDatasourceId(getDependsOn());
                }
            }
            standardDatasource.setSize(getSize());
            if (getVisible() != null) {
                N2oVisibilityDependency visibilityDependency = new N2oVisibilityDependency();
                visibilityDependency.setValue(StringUtils.unwrapLink(getVisible()));
                if (getDependsOn() != null) {
                    visibilityDependency.setDatasource(getDependsOn());//не учитывается, что виджет может использовать datasource из 7.19
                }
                visibilityDependency.setModel(ReduxModelEnum.RESOLVE);
                setDependencies(new N2oDependency[]{visibilityDependency});
            }
        }
    }

    @Override
    public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
        ids.putIfAbsent(prefix, 1);
        if (getId() == null)
            setId(prefix + ids.put(prefix, ids.get(prefix) + 1));
        result.add(this);
    }
}

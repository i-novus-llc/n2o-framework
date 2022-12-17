package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.RegionItem;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.action.UploadType;
import net.n2oapp.framework.api.metadata.aware.*;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
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
    private N2oStandardDatasource datasource;
    private Boolean fetchOnInit;
    @Deprecated
    private Integer size;
    private String cssClass;
    private String style;
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
    @N2oAttribute("Иконка")
    private String icon;
    @Deprecated
    private String masterFieldId;
    @Deprecated
    private String detailFieldId;
    @N2oAttribute("Условие видимости")
    private String visible;
    @Deprecated
    private N2oPreFilter[] preFilters;
    private ActionBar[] actions;
    private GenerateType actionGenerate;
    @N2oAttribute("Список меню управляющих кнопок")
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
                getUpload() != null || getDependsOn() != null) {
            N2oStandardDatasource datasource = new N2oStandardDatasource();
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
                N2oStandardDatasource.FetchDependency fetchDependency = new N2oStandardDatasource.FetchDependency();
                fetchDependency.setOn(getDependsOn());//не учитывается, что виджет может использовать datasource из 7.19
                fetchDependency.setModel(ReduxModel.resolve);
                datasource.setDependencies(new N2oStandardDatasource.Dependency[]{fetchDependency});
                //поддержка master-detail связи
                if (getDetailFieldId() != null) {
                    List<N2oPreFilter> preFilters = datasource.getFilters() == null ?
                            new ArrayList<>() :
                            new ArrayList<>(Arrays.asList(datasource.getFilters()));
                    String value = Placeholders.ref(getMasterFieldId() == null ? QuerySimpleField.PK : getMasterFieldId());
                    N2oPreFilter masterFilter = new N2oPreFilter(getDetailFieldId(), value, FilterType.eq);
                    String param = getMasterParam();
                    if (param == null && getRoute() != null && getRoute().contains(":")) {
                        param = getRoute().substring(getRoute().indexOf(":") + 1, getRoute().lastIndexOf("/"));
                    }
                    masterFilter.setParam(param);
                    masterFilter.setModel(ReduxModel.resolve);
                    masterFilter.setDatasourceId(getDependsOn());
                    masterFilter.setRequired(true);
                    preFilters.add(masterFilter);
                    datasource.setFilters(preFilters.toArray(new N2oPreFilter[0]));
                }
                if (datasource.getFilters() != null) {
                    for (N2oPreFilter filter : datasource.getFilters())
                        filter.setDatasourceId(getDependsOn());
                }
            }
            datasource.setSize(getSize());
            if (getVisible() != null) {
                N2oVisibilityDependency visibilityDependency = new N2oVisibilityDependency();
                visibilityDependency.setValue(StringUtils.unwrapLink(getVisible()));
                if (getDependsOn() != null) {
                    visibilityDependency.setDatasource(getDependsOn());//не учитывается, что виджет может использовать datasource из 7.19
                }
                visibilityDependency.setModel(ReduxModel.resolve);
                setDependencies(new N2oDependency[]{visibilityDependency});
            }
        }
    }

    @Override
    public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
        if (!ids.containsKey(prefix))
            ids.put(prefix, 1);
        if (getId() == null)
            setId(prefix + ids.put(prefix, ids.get(prefix) + 1));
        result.add(this);
    }
}

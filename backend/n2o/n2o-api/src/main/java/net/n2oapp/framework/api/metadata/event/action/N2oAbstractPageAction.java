package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQueryParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

import static net.n2oapp.framework.api.metadata.global.dao.N2oQuery.Field.PK;

/**
 * Абстрактное действие открытия страницы
 */
@Getter
@Setter
public abstract class N2oAbstractPageAction extends N2oAbstractAction implements PreFiltersAware {
    private String pageId;
    private String pageName;
    private String route;
    private String datasource;
    @Deprecated
    private UploadType upload;
    @Deprecated
    private String masterFieldId;
    @Deprecated
    private String detailFieldId;
    private String objectId;
    @Deprecated
    private String masterParam;
    //on close
    private Boolean refreshOnClose;
    private Boolean unsavedDataPromptOnClose;
    //on submit
    private String submitOperationId;
    private String submitLabel;
    private ReduxModel submitModel;
    private SubmitActionType submitActionType;
    private ReduxModel copyModel;
    private String copyDatasource;
    private String copyFieldId;
    private ReduxModel targetModel;
    private String targetDatasource;
    private CopyMode copyMode;
    private Boolean createMore;
    private Boolean closeAfterSubmit;
    private String redirectUrlAfterSubmit;
    private Target redirectTargetAfterSubmit;
    private Boolean refreshAfterSubmit;
    private String[] refreshDatasources;
    //on resolve
    private String labelFieldId;
    private String targetFieldId;
    private String valueFieldId;
    @Deprecated
    private String resultContainerId;
    @Deprecated
    private N2oPreFilter[] preFilters;
    private N2oParam[] params;
    private N2oDatasource[] datasources;

    @Deprecated
    private RefreshPolity refreshPolity;
    @Deprecated
    private String containerId;
    @Deprecated
    private Boolean refreshDependentContainer;
    @Deprecated
    private Boolean modalDictionary;
    @Deprecated
    private String width;
    @Deprecated
    private String minWidth;
    @Deprecated
    private String maxWidth;

    @Deprecated
    public void adaptV1() {
        if (getUpload() != null || getDetailFieldId() != null || getPreFilters() != null) {
            N2oDatasource datasource = new N2oDatasource();

            if (getUpload() != null) {
                switch (getUpload()) {
                    case query:
                        datasource.setDefaultValuesMode(DefaultValuesMode.query);
                        break;
                    case defaults:
                        datasource.setDefaultValuesMode(DefaultValuesMode.defaults);
                        break;
                    case copy:
                        datasource.setDefaultValuesMode(DefaultValuesMode.merge);
                        break;
                }
            }

            if (getDetailFieldId() != null && !UploadType.defaults.equals(getUpload())) {
                N2oPreFilter filter = new N2oPreFilter();
                filter.setFieldId(getDetailFieldId());
                filter.setType(FilterType.eq);
                String param = getMasterParam();
                if (param == null && getRoute() != null && getRoute().contains(":")) {
                    if (getRoute().indexOf(":") != getRoute().lastIndexOf(":"))
                        throw new N2oException(String.format("Невозможно определить параметр для detail-field-id в пути %s, необходимо задать master-param", getRoute()));
                    param = getRoute().substring(getRoute().indexOf(":") + 1, getRoute().lastIndexOf("/"));
                }
                if (param == null) {
                    param = "$widgetId_" + getDetailFieldId();
                }
                if (getRoute() != null && getRoute().contains(":" + param)) {
                    N2oPathParam pathParam = new N2oPathParam();
                    pathParam.setName(param);
                    pathParam.setDatasource(filter.getDatasource());
                    pathParam.setModel(filter.getModel());
                    pathParam.setValue(filter.getValueAttr());
                    boolean exists = false;
                    if (getPathParams() != null) {
                        for (N2oPathParam oldPathParam : getPathParams()) {
                            if (oldPathParam.getName().equals(param)) {
                                exists = true;
                                break;
                            }
                        }
                    }
                    if (!exists)
                        addPathParams(new N2oPathParam[]{pathParam});
                } else if (!ReduxModel.filter.equals(filter.getModel())) {
                    N2oQueryParam queryParam = new N2oQueryParam();
                    queryParam.setName(param);
                    queryParam.setDatasource(filter.getDatasource());
                    queryParam.setModel(filter.getModel());
                    queryParam.setValue(filter.getValueAttr());
                    boolean exists = false;
                    if (getQueryParams() != null) {
                        for (N2oQueryParam oldQueryParam : getQueryParams()) {
                            if (oldQueryParam.getName().equals(param)) {
                                exists = true;
                                break;
                            }
                        }
                    }
                    if (!exists)
                        addQueryParams(new N2oQueryParam[]{queryParam});
                }
                filter.setParam(param);
                filter.setValueAttr(Placeholders.ref(getMasterFieldId() != null ? getMasterFieldId() : PK));
                datasource.addFilters(List.of(filter));
            }

            if (preFilters != null) {
                datasource.addFilters(Arrays.asList(preFilters));
            }

            datasources = new N2oDatasource[]{datasource};

        }
    }

    @Override
    public String getOperationId() {
        return submitOperationId;
    }

    public N2oPathParam[] getPathParams() {
        if (this.params == null) {
            return null;
        }
        return Arrays.stream(this.params).filter(p -> p instanceof N2oPathParam).toArray(N2oPathParam[]::new);
    }

    public N2oQueryParam[] getQueryParams() {
        if (this.params == null) {
            return null;
        }
        return Arrays.stream(this.params).filter(p -> p instanceof N2oQueryParam).toArray(N2oQueryParam[]::new);
    }

    public void addPathParams(N2oPathParam[] pathParams) {
        if (this.params == null)
            this.params = new N2oParam[0];
        this.params = ArrayUtils.addAll(this.params, pathParams);
    }

    public void addQueryParams(N2oQueryParam[] queryParams) {
        if (this.params == null)
            this.params = new N2oParam[0];
        this.params = ArrayUtils.addAll(this.params, queryParams);
    }

    @Deprecated
    public String getRefreshWidgetId() {
        return refreshDatasources == null ? null : refreshDatasources[0];
    }

    @Deprecated
    public void setRefreshWidgetId(String refreshWidgetId) {
        this.refreshDatasources = new String[]{refreshWidgetId};
    }

    @Deprecated
    public String getTargetWidgetId() {
        return targetDatasource;
    }

    @Deprecated
    public void setTargetWidgetId(String targetWidgetId) {
        this.targetDatasource = targetWidgetId;
    }

    @Deprecated
    public String getCopyWidgetId() {
        return copyDatasource;
    }

    @Deprecated
    public void setCopyWidgetId(String copyWidgetId) {
        this.copyDatasource = copyWidgetId;
    }
}

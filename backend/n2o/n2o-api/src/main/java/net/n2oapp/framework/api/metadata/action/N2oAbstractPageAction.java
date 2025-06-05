package net.n2oapp.framework.api.metadata.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.PageRefEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQueryParam;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBreadcrumb;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyModeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

import static net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField.PK;

/**
 * Абстрактное действие открытия страницы
 */
@Getter
@Setter
public abstract class N2oAbstractPageAction extends N2oAbstractAction implements PreFiltersAware {
    private String pageId;
    private String pageName;
    private String route;
    private TargetEnum target;
    @Deprecated
    private DefaultValuesModeEnum mode;
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
    @Deprecated
    private String submitOperationId;
    @Deprecated
    private String submitLabel;
    @Deprecated
    private ReduxModelEnum submitModel;
    @Deprecated
    private SubmitActionTypeEnum submitActionType;
    @Deprecated
    private Boolean submitMessageOnSuccess;
    @Deprecated
    private Boolean submitMessageOnFail;

    private CopyModeEnum copyMode;
    private String copyDatasourceId;
    private ReduxModelEnum copyModel;
    private String copyFieldId;
    private String targetDatasourceId;
    private ReduxModelEnum targetModel;
    private String targetFieldId;
    private PageRefEnum targetPage;

    @Deprecated
    private Boolean closeAfterSubmit;
    @Deprecated
    private String redirectUrlAfterSubmit;
    @Deprecated
    private TargetEnum redirectTargetAfterSubmit;
    @Deprecated
    private Boolean refreshAfterSubmit;
    @JsonProperty("refreshDatasources")
    private String[] refreshDatasourceIds;
    //on resolve
    private String labelFieldId;
    private String valueFieldId;
    @Deprecated
    private N2oPreFilter[] preFilters;
    private N2oParam[] params;
    private N2oAbstractDatasource[] datasources;
    private N2oBreadcrumb[] breadcrumbs;
    private N2oToolbar[] toolbars;
    private ActionBar[] actions;
    @Deprecated
    private String width;

    @Deprecated // при удалении убрать, N2oStandardDatasourceMerger, а также его вызов в PageCompiler
    public void adaptV1() {
        if (getMode() != null || getDetailFieldId() != null || getPreFilters() != null) {
            N2oStandardDatasource datasource = new N2oStandardDatasource();

            if (getMode() != null) {
                switch (getMode()) {
                    case QUERY:
                        datasource.setDefaultValuesMode(DefaultValuesModeEnum.QUERY);
                        break;
                    case DEFAULTS:
                        datasource.setDefaultValuesMode(DefaultValuesModeEnum.DEFAULTS);
                        break;
                    case MERGE:
                        datasource.setDefaultValuesMode(DefaultValuesModeEnum.MERGE);
                        break;
                }
            }

            if (getDetailFieldId() != null && !DefaultValuesModeEnum.DEFAULTS.equals(getMode())) {
                N2oPreFilter filter = new N2oPreFilter();
                filter.setFieldId(getDetailFieldId());
                filter.setType(FilterTypeEnum.EQ);
                filter.setValueAttr(Placeholders.ref(getMasterFieldId() != null ? getMasterFieldId() : PK));
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
                    pathParam.setDatasourceId(filter.getDatasourceId());
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
                } else if (!ReduxModelEnum.FILTER.equals(filter.getModel())) {
                    N2oQueryParam queryParam = new N2oQueryParam();
                    queryParam.setName(param);
                    queryParam.setDatasourceId(filter.getDatasourceId());
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
                datasource.addFilters(List.of(filter));
            }

            if (preFilters != null) {
                datasource.addFilters(Arrays.asList(preFilters));
            }

            datasources = new N2oStandardDatasource[]{datasource};

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
        return Arrays.stream(this.params).filter(N2oPathParam.class::isInstance).toArray(N2oPathParam[]::new);
    }

    public N2oQueryParam[] getQueryParams() {
        if (this.params == null) {
            return null;
        }
        return Arrays.stream(this.params).filter(N2oQueryParam.class::isInstance).toArray(N2oQueryParam[]::new);
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
        return refreshDatasourceIds == null ? null : refreshDatasourceIds[0];
    }

    @Deprecated
    public void setRefreshWidgetId(String refreshWidgetId) {
        this.refreshDatasourceIds = new String[]{refreshWidgetId};
    }

    @Deprecated
    public String getTargetWidgetId() {
        return targetDatasourceId;
    }

    @Deprecated
    public void setTargetWidgetId(String targetWidgetId) {
        this.targetDatasourceId = targetWidgetId;
    }

    @Deprecated
    public String getCopyWidgetId() {
        return copyDatasourceId;
    }

    @Deprecated
    public void setCopyWidgetId(String copyWidgetId) {
        this.copyDatasourceId = copyWidgetId;
    }
}

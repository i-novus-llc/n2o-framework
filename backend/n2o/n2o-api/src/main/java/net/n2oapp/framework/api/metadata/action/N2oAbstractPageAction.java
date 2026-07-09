package net.n2oapp.framework.api.metadata.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.control.PageRefEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oQueryParam;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBreadcrumb;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyModeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * Абстрактное действие открытия страницы
 */
@Getter
@Setter
public abstract class N2oAbstractPageAction extends N2oAbstractAction {
    private String pageId;
    private String pageName;
    private String route;
    @Deprecated
    private TargetEnum target;
    private String objectId;
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
    private N2oParam[] params;
    private N2oAbstractDatasource[] datasources;
    private N2oBreadcrumb[] breadcrumbs;
    private N2oToolbar[] toolbars;
    private ActionBar[] actions;
    @Deprecated
    private String width;

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
}

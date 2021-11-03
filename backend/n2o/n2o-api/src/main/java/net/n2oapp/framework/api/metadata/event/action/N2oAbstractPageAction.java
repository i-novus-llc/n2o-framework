package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQueryParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

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
    @Deprecated
    private String copyWidgetId;
    private String copyDatasource;
    private String copyFieldId;
    private ReduxModel targetModel;
    @Deprecated
    private String targetWidgetId;
    private String targetDatasource;
    private CopyMode copyMode;
    private Boolean createMore;
    private Boolean focusAfterSubmit;
    private Boolean closeAfterSubmit;
    private String redirectUrlAfterSubmit;
    private Target redirectTargetAfterSubmit;
    private Boolean refreshAfterSubmit;
    @Deprecated
    private String refreshWidgetId;
    private String[] refreshDatasources;
    //on resolve
    private String labelFieldId;
    private String targetFieldId;
    private String valueFieldId;

    //todo rename to resultWidgetId
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

    @Override
    public String getOperationId() {
        return submitOperationId;
    }

    public N2oParam[] getPathParams() {
        return Arrays.stream(this.params).filter(p -> p instanceof N2oPathParam).toArray(N2oParam[]::new);
    }

    public N2oParam[] getQueryParams() {
        return Arrays.stream(this.params).filter(p -> p instanceof N2oQueryParam).toArray(N2oParam[]::new);
    }

    public void setPathParams(N2oParam[] pathParams) {
        ArrayUtils.addAll(this.params, pathParams);
    }

    public void setQueryParams(N2oParam[] pathParams) {
        ArrayUtils.addAll(this.params, pathParams);
    }
}

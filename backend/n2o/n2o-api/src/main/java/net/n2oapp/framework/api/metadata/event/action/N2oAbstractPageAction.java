package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;

/**
 * Абстрактное действие открытия страницы
 */
@Getter
@Setter
public abstract class N2oAbstractPageAction extends N2oAbstractAction implements PreFiltersAware {
    private String pageId;
    private String pageName;
    private String route;
    private Target target;
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
    private String copyWidgetId;
    private String copyFieldId;
    private ReduxModel targetModel;
    private String targetWidgetId;
    private CopyMode copyMode;
    private Boolean createMore;
    private Boolean focusAfterSubmit;
    private Boolean closeAfterSubmit;
    private String redirectUrlAfterSubmit;
    private Target redirectTargetAfterSubmit;
    private Boolean refreshAfterSubmit;
    private String refreshWidgetId;
    //on resolve
    private String labelFieldId;
    private String targetFieldId;
    private String valueFieldId;

    //todo rename to resultWidgetId
    private String resultContainerId;
    @Deprecated
    private N2oPreFilter[] preFilters;
    private N2oParam[] queryParams;
    private N2oParam[] pathParams;

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
}

package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreField;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Абстрактное действие открытия страницы
 */
@Getter
@Setter
public abstract class N2oAbstractPageAction extends N2oAbstractAction {
    private String pageId;
    private String pageName;
    private String route;
    private UploadType upload;
    private String masterFieldId;
    private String detailFieldId;
    private String objectId;
    private String masterParam;
    //on close
    private Boolean refreshOnClose;
    private Boolean unsavedDataPromptOnClose;
    //on submit
    private String submitOperationId;
    private String submitLabel;
    private ReduxModel submitModel;
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
    private N2oPreFilter[] preFilters;
    private N2oPreField[] preFields;

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

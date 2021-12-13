package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.action.DefaultActions;

import java.util.Collections;
import java.util.List;

public abstract class AbstractButtonGenerator implements ButtonGenerator {

    protected List<ToolbarItem> build(DefaultActions action, CompileProcessor p) {
        N2oButton button = new N2oButton();
        button.setId(action.name());
        button.setLabel(p.getMessage(action.getLabel()));
        button.setIcon(action.getIcon());
        if (action.isContext()) {
            button.setModel(ReduxModel.RESOLVE);
        } else {
            button.setModel(ReduxModel.FILTER);
        }
        switch (action) {
            case delete: {
                N2oInvokeAction invokeAction = new N2oInvokeAction();
                invokeAction.setOperationId(action.name());
                button.setConfirm(true);
                button.setAction(invokeAction);
            }
            break;
            case create:{
                N2oShowModal modal = new N2oShowModal();
                CompiledObject object = p.getScope(CompiledObject.class);
                modal.setPageId(object.getId());
                modal.setObjectId(object.getId());
                modal.setPageName(p.getMessage(action.getPageName(), object.getName()));
                modal.setSubmitOperationId(action.name());
                modal.setUpload(action.getUpload());
                modal.setFocusAfterSubmit(true);
                modal.setCloseAfterSubmit(true);
                button.setAction(modal);
            }
            break;
            case update: {
                N2oShowModal modal = new N2oShowModal();
                CompiledObject object = p.getScope(CompiledObject.class);
                modal.setPageId(object.getId());
                modal.setObjectId(object.getId());
                modal.setPageName(p.getMessage(action.getPageName(), object.getName()));
                modal.setMasterFieldId(N2oQuery.Field.PK);
                modal.setDetailFieldId(N2oQuery.Field.PK);
                String paramName = button.getWidgetId() + "_" + N2oQuery.Field.PK;
                modal.setRoute("/:" + paramName + "/update");
                N2oPathParam pathParam = new N2oPathParam();
                pathParam.setName(paramName);
                pathParam.setValue(Placeholders.ref(N2oQuery.Field.PK));
                modal.setPathParams(new N2oPathParam[]{pathParam});
                modal.setSubmitOperationId(action.name());
                modal.setUpload(action.getUpload());
                modal.setFocusAfterSubmit(true);
                modal.setCloseAfterSubmit(true);
                button.setAction(modal);
            }
            break;
            default:
                throw new UnsupportedOperationException();
        }
        return Collections.singletonList(button);
    }
}

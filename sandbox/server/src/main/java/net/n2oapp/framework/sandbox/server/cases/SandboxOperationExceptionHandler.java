package net.n2oapp.framework.sandbox.server.cases;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import org.springframework.stereotype.Component;

import javax.persistence.NonUniqueResultException;

@Component
public class SandboxOperationExceptionHandler implements OperationExceptionHandler {
    @Override
    public N2oException handle(CompiledObject.Operation o, DataSet data, Exception e) {
        if (e instanceof N2oException) {
            if (e.getCause() instanceof NonUniqueResultException)
                return notUniqueDialog(data);
            else
                throw (N2oException) e;
        } else
            return new N2oException(e);
    }

    private N2oException notUniqueDialog(DataSet data) {
        N2oDialog dialog = getNotUniqueDialog(data);
        N2oException e = new N2oException();
        e.setDialog(dialog);
        e.setHttpStatus(400);
        return e;
    }

    public static N2oDialog getNotUniqueDialog(DataSet data) {
        N2oDialog dialog = new N2oDialog("notUnique");
        dialog.setTitle("Внимание");
        dialog.setDescription(data.getString("name") + " уже зарегистрирован, что делать?");
        dialog.setSize("lg");

        N2oFormParam nameParam = new N2oFormParam();
        nameParam.setId("name");
        nameParam.setValue(data.getString("name"));

        N2oButton registerNew = new N2oButton();
        registerNew.setId("registerNew");
        registerNew.setSrc("StandardButton");
        registerNew.setLabel("Зарегистрировать нового");
        registerNew.setModel(ReduxModel.resolve);
        N2oInvokeAction registerNewAction = new N2oInvokeAction();
        registerNewAction.setOperationId("register");
        registerNewAction.setDoubleCloseOnSuccess(true);
        registerNewAction.setCloseOnFail(true);
        N2oParam registerNewParam = new N2oParam();
        registerNewParam.setName("join");
        registerNewParam.setValue("false");
        registerNewAction.setHeaderParams(new N2oParam[]{registerNewParam});
        registerNewAction.setFormParams(new N2oFormParam[]{nameParam});
        registerNew.setAction(registerNewAction);

        N2oButton useExists = new N2oButton();
        useExists.setId("useExists");
        useExists.setSrc("StandardButton");
        useExists.setLabel("Использовать существующего");
        useExists.setModel(ReduxModel.resolve);
        N2oInvokeAction useExistsAction = new N2oInvokeAction();
        useExistsAction.setOperationId("register");
        useExistsAction.setDoubleCloseOnSuccess(true);
        useExistsAction.setCloseOnFail(true);
        N2oParam useExistsParam = new N2oParam();
        useExistsParam.setName("join");
        useExistsParam.setValue("true");
        useExistsAction.setHeaderParams(new N2oParam[]{useExistsParam});
        useExistsAction.setFormParams(new N2oFormParam[]{nameParam});
        useExists.setAction(useExistsAction);

        N2oButton cancel = new N2oButton();
        cancel.setId("cancel");
        cancel.setSrc("StandardButton");
        cancel.setLabel("Отмена");
        cancel.setModel(ReduxModel.resolve);
        cancel.setAction(new N2oCloseAction());

        dialog.setToolbar(new N2oToolbar());
        dialog.getToolbar().setItems(new N2oButton[]{registerNew, useExists, cancel});
        return dialog;
    }
}

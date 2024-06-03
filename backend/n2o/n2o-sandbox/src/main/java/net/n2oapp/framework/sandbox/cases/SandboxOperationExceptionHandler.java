package net.n2oapp.framework.sandbox.cases;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.boot.graphql.N2oGraphQlException;
import org.springframework.stereotype.Component;

import jakarta.persistence.NonUniqueResultException;

@Component
public class SandboxOperationExceptionHandler implements OperationExceptionHandler {
    @Override
    public N2oException handle(CompiledObject.Operation o, DataSet data, Exception e) {
        if (e instanceof N2oGraphQlException)
            return GraphQlUtil.constructErrorMessage((N2oGraphQlException) e);

        if (e instanceof N2oException) {
            if (e.getCause() instanceof NonUniqueResultException)
                return notUniqueDialog(data);
            else
                return addFailInfo(o, data, (N2oException) e);
        } else
            return addFailInfo(o, data, new N2oException(e));
    }

    private N2oException addFailInfo(CompiledObject.Operation o, DataSet data, N2oException e) {
        if (o.getFailText() != null) {
            e.setUserMessage(StringUtils.resolveLinks(o.getFailText(), data));
        }
        if (o.getFailTitle() != null)
            e.setUserMessageTitle(StringUtils.resolveLinks(o.getFailTitle(), data));
        return e;
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
        nameParam.setName("name");
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
        registerNew.setActions(new N2oInvokeAction[]{registerNewAction});

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
        useExists.setActions(new N2oInvokeAction[]{useExistsAction});

        N2oButton cancel = new N2oButton();
        cancel.setId("cancel");
        cancel.setSrc("StandardButton");
        cancel.setLabel("Отмена");
        cancel.setModel(ReduxModel.resolve);
        cancel.setActions(new N2oCloseAction[]{new N2oCloseAction()});

        dialog.setToolbar(new N2oToolbar());
        dialog.getToolbar().setItems(new N2oButton[]{registerNew, useExists, cancel});
        return dialog;
    }
}

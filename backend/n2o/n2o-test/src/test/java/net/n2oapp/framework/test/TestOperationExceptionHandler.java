package net.n2oapp.framework.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

public class TestOperationExceptionHandler implements OperationExceptionHandler {
    @Override
    public N2oException handle(CompiledObject.Operation o, DataSet data, Exception e) {
        N2oException exception = new N2oException();
        N2oDialog dialog = new N2oDialog("testDialog");
        dialog.setTitle("Registration accept");
        dialog.setDescription("Are you sure?");
        N2oButton[] buttons = new N2oButton[2];
        buttons[0] = new N2oButton();
        buttons[0].setId("yes");
        buttons[0].setLabel("Yes");
        N2oInvokeAction action = new N2oInvokeAction();
        action.setOperationId("create");
        action.setObjectId("testDialog");
        action.setRoute("/create");
        buttons[0].setAction(action);
        buttons[1] = new N2oButton();
        buttons[0].setId("no");
        buttons[1].setLabel("No");
        N2oCloseAction action1 = new N2oCloseAction();
        buttons[1].setAction(action1);
        buttons[1].setModel(ReduxModel.filter);
        dialog.setToolbar(new N2oToolbar(buttons));
        exception.setDialog(dialog);
        return exception;
    }
}

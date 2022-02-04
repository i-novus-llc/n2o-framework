package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ValidateType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Генерация кнопки Отправить
 */
@Component
public class SubmitGenerator implements ButtonGenerator {
    @Override
    public String getCode() {
        return "submit";
    }

    @Override
    public List<ToolbarItem> generate(N2oToolbar toolbar, CompileContext context, CompileProcessor p) {
        if (!(context instanceof PageContext))
            throw new IllegalStateException("Need PageContext");
        String widgetId = toolbar.getTargetWidgetId();
        if (widgetId == null) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            widgetId = widgetScope == null ? null : widgetScope.getClientWidgetId();
        }
        PageContext pageContext = (PageContext) context;

        N2oButton saveButton = new N2oButton();
        saveButton.setId(GenerateType.submit.name());
        saveButton.setColor("primary");
        N2oAction action = null;
        ReduxModel saveButtonModel = null;
        SubmitActionType submitActionType = pageContext.getSubmitActionType() == null ? SubmitActionType.invoke : pageContext.getSubmitActionType();
        String submitLabel = pageContext.getSubmitLabel();

        switch (submitActionType) {
            case copy: {
                N2oCopyAction copyAction = new N2oCopyAction();
                copyAction.setSourceModel(pageContext.getCopyModel());
                copyAction.setSourceWidgetId(pageContext.getCopyWidgetId());
                copyAction.setSourceFieldId(pageContext.getCopyFieldId());
                copyAction.setTargetModel(pageContext.getTargetModel());
                copyAction.setTargetWidgetId(pageContext.getTargetWidgetId() == null ?
                        pageContext.getParentWidgetId() : pageContext.getTargetWidgetId());
                copyAction.setTargetClientPageId(pageContext.getParentClientPageId());
                copyAction.setTargetFieldId(pageContext.getTargetFieldId());
                copyAction.setMode(pageContext.getCopyMode());
                action = copyAction;
                saveButtonModel = ((PageContext) context).getCopyModel();
            }
            break;
            case invoke: {
                N2oInvokeAction invokeAction = new N2oInvokeAction();
                invokeAction.setCloseOnSuccess(pageContext.getCloseOnSuccessSubmit());
                invokeAction.setRedirectTarget(pageContext.getRedirectTargetOnSuccessSubmit());
                invokeAction.setRedirectUrl(pageContext.getRedirectUrlOnSuccessSubmit());
                invokeAction.setRefreshOnSuccess(pageContext.getRefreshOnSuccessSubmit());
                invokeAction.setOperationId(pageContext.getSubmitOperationId());

                CompiledObject compiledObject = p.getScope(CompiledObject.class);
                if (compiledObject != null && compiledObject.getOperations().containsKey(pageContext.getSubmitOperationId())) {
                    Boolean confirm = compiledObject.getOperations().get(pageContext.getSubmitOperationId()).getConfirm();
                    saveButton.setConfirm(confirm != null ? confirm.toString() : null);
                    if (submitLabel == null) {
                        submitLabel = compiledObject.getOperations().get(pageContext.getSubmitOperationId()).getFormSubmitLabel();
                    }
                }
                action = invokeAction;
                saveButtonModel = ((PageContext) context).getSubmitModel();
            }
            break;
        }
        saveButton.setLabel(p.cast(submitLabel, p.getMessage("n2o.api.action.toolbar.button.submit.label")));
        saveButton.setWidgetId(widgetId);
        saveButton.setAction(action);
        saveButton.setModel(p.cast(saveButtonModel, ReduxModel.RESOLVE));
        saveButton.setValidate(ValidateType.WIDGET);
        return Collections.singletonList(saveButton);
    }
}

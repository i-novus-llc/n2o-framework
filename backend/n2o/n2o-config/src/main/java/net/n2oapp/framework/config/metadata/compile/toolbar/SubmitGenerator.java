package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
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
        String widgetId = toolbar.getTargetWidgetId();
        if (widgetId == null) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            widgetId = widgetScope == null ? null : widgetScope.getClientWidgetId();
        }
        String submitOperationId = null;
        ReduxModel submitModel = null;
        String submitLabel = null;
        if (context instanceof PageContext) {
            submitOperationId = ((PageContext) context).getSubmitOperationId();
            submitModel = ((PageContext) context).getSubmitModel();
            submitLabel = ((PageContext) context).getSubmitLabel();
        }
        if (submitLabel == null) {
            CompiledObject compiledObject = p.getScope(CompiledObject.class);
            if (compiledObject != null) {
                CompiledObject.Operation operation = compiledObject.getOperations().get(submitOperationId);
                if (operation != null)
                    submitLabel = operation.getFormSubmitLabel();
            }
        }
        N2oButton saveButton = new N2oButton();
        saveButton.setId(GenerateType.submit.name());
        saveButton.setLabel(p.cast(submitLabel, p.getMessage("n2o.api.action.toolbar.button.submit.label")));
        saveButton.setPrimary(true);
        saveButton.setColor("primary");
        N2oInvokeAction saveAction = new N2oInvokeAction();
        if (context instanceof PageContext) {
            PageContext pageContext = (PageContext) context;
            saveAction.setCloseOnSuccess(pageContext.getCloseOnSuccessSubmit());
            saveAction.setRedirectTarget(pageContext.getRedirectTargetOnSuccessSubmit());
            saveAction.setRedirectUrl(pageContext.getRedirectUrlOnSuccessSubmit());
            saveAction.setRefreshOnSuccess(pageContext.getRefreshOnSuccessSubmit());
        }
        saveButton.setWidgetId(widgetId);
        saveButton.setModel(ReduxModel.RESOLVE);
        saveAction.setOperationId(submitOperationId);
        saveButton.setAction(saveAction);
        saveButton.setModel(p.cast(submitModel, ReduxModel.RESOLVE));
        saveButton.setValidate(true);
        saveButton.setConfirm(false);
        return Collections.singletonList(saveButton);
    }
}

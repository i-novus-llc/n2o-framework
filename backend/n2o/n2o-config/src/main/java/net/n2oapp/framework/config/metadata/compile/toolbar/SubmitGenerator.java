package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
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
        String datasource = toolbar.getDatasourceId();
        N2oButton saveButton = new N2oButton();
        saveButton.setId(GenerateType.submit.name());
        saveButton.setColor("primary");
        N2oInvokeAction invokeAction = new N2oInvokeAction();
        invokeAction.setCloseOnSuccess(false);
        invokeAction.setRefreshOnSuccess(true);
        invokeAction.setOperationId("submit");
        saveButton.setLabel(p.getMessage("n2o.api.action.toolbar.button.submit.label"));
        saveButton.setDatasourceId(datasource);
        saveButton.setActions(new N2oAction[]{invokeAction});
        saveButton.setModel(ReduxModel.resolve);
        saveButton.setValidate(true);
        return Collections.singletonList(saveButton);
    }
}

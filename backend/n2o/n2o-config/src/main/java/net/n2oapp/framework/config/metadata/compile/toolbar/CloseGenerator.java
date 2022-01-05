package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Генерация кнопки Закрыть
 */
@Component
public class CloseGenerator implements ButtonGenerator {
    @Override
    public String getCode() {
        return "close";
    }

    @Override
    public List<ToolbarItem> generate(N2oToolbar toolbar, CompileContext context, CompileProcessor p) {
        String datasource = toolbar.getDatasource();
        N2oButton closeButton = new N2oButton();
        closeButton.setId(GenerateType.close.name());
        closeButton.setLabel(p.getMessage("n2o.api.action.toolbar.button.close.label"));
        N2oCloseAction cancelAction = new N2oCloseAction();
        cancelAction.setId(GenerateType.close.name());
        closeButton.setDatasource(datasource);
        closeButton.setModel(ReduxModel.filter);
        if (context instanceof PageContext) {
            cancelAction.setRefreshOnClose(((PageContext) context).getRefreshOnClose());
        }
        closeButton.setAction(cancelAction);
        return Collections.singletonList(closeButton);
    }
}

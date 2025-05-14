package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
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
        String datasource = toolbar.getDatasourceId();
        N2oButton closeButton = new N2oButton();
        closeButton.setId(GenerateTypeEnum.close.name());
        closeButton.setLabel(p.getMessage("n2o.api.action.toolbar.button.close.label"));
        N2oCloseAction cancelAction = new N2oCloseAction();
        cancelAction.setId(GenerateTypeEnum.close.name());
        closeButton.setDatasourceId(datasource);
        closeButton.setModel(ReduxModelEnum.filter);
        if (context instanceof PageContext pageContext)
            cancelAction.setRefresh(pageContext.getRefreshOnClose());
        closeButton.setActions(new N2oCloseAction[]{cancelAction});
        closeButton.setValidate(false);
        return Collections.singletonList(closeButton);
    }
}

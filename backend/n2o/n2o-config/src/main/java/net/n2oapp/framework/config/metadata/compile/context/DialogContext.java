package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.meta.page.Dialog;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;

/**
 * Контекст компиляции диалога подтверждения действия
 */
@Getter
@Setter
public class DialogContext extends BaseCompileContext<Dialog, N2oDialog> {

    /**
     * Идентификатор родительской страницы
     */
    private String parentPageId;
    /**
     * Идентификатор родительского источника данных
     */
    private String parentSourceDatasourceId;

    /**
     * Идентификатор родительского виджета
     */
    private String parentClientWidgetId;

    /**
     * Идентификатор объекта, в котором находится операция
     */
    private String objectId;

    /**
     * Редирект после успешного выполнеия
     */
    private RedirectSaga parentRedirect;

    /**
     * Обновление после успешного выполнения
     */
    private RefreshSaga parentRefresh;

    public DialogContext(String route, String sourceId) {
        super(route, sourceId, N2oDialog.class, Dialog.class);
    }
}

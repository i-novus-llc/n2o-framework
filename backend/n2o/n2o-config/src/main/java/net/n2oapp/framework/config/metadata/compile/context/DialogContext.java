package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oDialog;
import net.n2oapp.framework.api.metadata.meta.Dialog;

/**
 * Контекст компиляции диалога подтверждения действия
 */
@Getter
@Setter
public class DialogContext extends BaseCompileContext<Dialog, N2oDialog> {

    private String clientWidgetId;
    private String objectId;

    public DialogContext(String sourceId, String clientWidgetId, String objectId) {
        super(sourceId, N2oDialog.class, Dialog.class);
        this.clientWidgetId = clientWidgetId;
        this.objectId = objectId;
    }
}

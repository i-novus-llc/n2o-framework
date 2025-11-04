package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.N2oAction;

import java.util.List;

/**
 * Исходная модель кнопки {@code <clipboard-button>}
 */
@Getter
@Setter
public class N2oClipboardButton extends N2oAbstractButton {
    private String data;
    private ClipboardButtonDataEnum type;
    private String message;
    private Dependency[] dependencies;
    private DisableOnEmptyModelType disableOnEmptyModel;

    @Override
    public List<N2oAction> getListActions() {
        return List.of();
    }
}

package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;

/**
 * Исходная модель виджета Форма
 */
@Getter
@Setter
public class N2oForm extends N2oWidget {
    private SourceComponent[] items;
    private FormModeEnum mode;
    private Boolean unsavedDataPrompt;
}

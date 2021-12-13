package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.Submit;

/**
 * Исходная модель виджета Форма
 */
@Getter
@Setter
public class N2oForm extends N2oWidget {
    private String modalWidth;
    private String layout;
    private SourceComponent[] items;
    private FormMode mode;
    private Boolean prompt;
    @Deprecated
    private Submit submit;
}

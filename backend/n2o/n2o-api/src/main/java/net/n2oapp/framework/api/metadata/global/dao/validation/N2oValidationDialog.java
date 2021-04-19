package net.n2oapp.framework.api.metadata.global.dao.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * Исходная модель валидации с диалогом выбора
 */
@Getter
@Setter
public class N2oValidationDialog extends N2oInvocationValidation {
    private String size;
    private N2oToolbar toolbar;
}

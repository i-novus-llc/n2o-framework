package net.n2oapp.framework.api.metadata.global.dao.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * Исходная модель валидации с диалогом выбора
 */
@Getter
@Setter
public class N2oValidationDialog extends N2oValidation {
    private String result;
    private String size;
    private N2oInvocation n2oInvocation;
    private N2oObject.Parameter[] inParameters;
    private N2oObject.Parameter[] outParameters;
    private N2oToolbar toolbar;
}

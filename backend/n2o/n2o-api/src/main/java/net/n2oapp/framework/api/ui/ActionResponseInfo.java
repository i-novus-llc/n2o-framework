package net.n2oapp.framework.api.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * Информация об ответе выполненной операции
 */
@Getter
@Setter
public class ActionResponseInfo extends ResponseInfo {
    private Boolean success;
}

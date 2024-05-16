package net.n2oapp.framework.api.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * Информация об ответе выполненной проверки
 */
@Getter
@Setter
public class ValidationResponseInfo extends ResponseInfo {

    private Boolean success;

}

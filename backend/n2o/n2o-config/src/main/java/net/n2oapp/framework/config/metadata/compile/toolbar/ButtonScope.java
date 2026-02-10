package net.n2oapp.framework.config.metadata.compile.toolbar;

import lombok.Getter;

/**
 * Информация о кнопке
 */
@Getter
public class ButtonScope {
    Boolean validate;

    public ButtonScope(Boolean validate) {
        this.validate = validate;
    }
}

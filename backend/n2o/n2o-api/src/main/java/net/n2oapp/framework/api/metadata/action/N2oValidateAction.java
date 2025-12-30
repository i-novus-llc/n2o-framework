package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

/**
 * Исходная модель действия валидации
 */
@Getter
@Setter
public class N2oValidateAction extends N2oAbstractAction {
    private String datasource;
    private ReduxModelEnum model;
    private ValidateBreakOnEnum breakOn;
    private Field[] fields;

    @Getter
    @Setter
    public static class Field {
        private String id;
    }

}

package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;

import java.util.List;


/**
 * Абстратная реализация контрола
 */
@Getter
@Setter
public abstract class N2oStandardField extends N2oField {

    private String placeholder;
    private Boolean copied;
    private Validations validations;
    private String defaultValue;

    public void setActionButtons(List<N2oActionButton> buttons) {
        //todo добавлять их в тулбар
    }

}

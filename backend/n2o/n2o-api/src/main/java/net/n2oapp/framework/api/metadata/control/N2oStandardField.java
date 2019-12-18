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
    private String controlSrc;
    private Validations validations;
    private String defaultValue;
    private String filterId;

    @Override
    public void setSrc(String src) {
        this.controlSrc = src;
    }

    @Override
    public String getSrc() {
        return controlSrc;
    }

    public void setActionButtons(List<N2oActionButton> buttons) {
        //todo добавлять их в тулбар
    }

}

package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.view.CssClassAware;

import java.util.List;


/**
 * Абстратная реализация контрола
 */
@Getter
@Setter
public abstract class N2oStandardField extends N2oField implements CssClassAware {
    private String placeholder;
    private Boolean copied;
    private String controlSrc;

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

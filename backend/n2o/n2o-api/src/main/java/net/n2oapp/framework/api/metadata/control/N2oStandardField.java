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
    private String label;
    private String description;
    private String placeholder;
    private String help;
    private String defaultValue;//значение, при model="default"
    private String cssClass;
    private String style;
    private String labelStyle;
    private String labelClass;
    private Boolean noLabel;
    private String domain;
    private Boolean copied;
    private N2oToolbar toolbar;

    public void setActionButtons(List<N2oActionButton> buttons) {
        //todo добавлять их в тулбар
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getId() + ")";
    }

}

package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;

@Getter
@Setter
public class N2oConfirmAction extends N2oAbstractAction implements N2oAction{

    private String title;

    private String text;

    private String className;

    private String style;

    private ConfirmType type;

    private Boolean closeButton;

    private ConfirmButton[] confirmButtons;

    @Getter
    @Setter
    public abstract static class ConfirmButton extends N2oComponent {

        private String label;
        private String color;
        private String icon;

    }

    public static class OkButton extends ConfirmButton {}

    public static class CancelButton extends ConfirmButton {}
}

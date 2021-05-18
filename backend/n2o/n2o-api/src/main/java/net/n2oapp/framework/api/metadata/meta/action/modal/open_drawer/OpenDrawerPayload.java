package net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.modal.ModalPayload;

/**
 * Клиентская модель компонента open-drawer
 */
@Getter
@Setter
public class OpenDrawerPayload extends ModalPayload {
    @JsonProperty
    private String mode = "drawer";
    @JsonProperty
    private Boolean closable = true;
    @JsonProperty
    private Boolean backdrop;
    @JsonProperty
    private String width;
    @JsonProperty
    private String height;
    @JsonProperty
    private String placement;
    @JsonProperty
    private String level;
    @JsonProperty
    private Boolean closeOnBackdrop;
    @JsonProperty
    private Boolean prompt;
    @JsonProperty
    private Boolean fixedFooter;
    @JsonProperty
    private Boolean closeOnEscape;
}

package net.n2oapp.framework.api.metadata.meta.action.modal.show_modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.modal.ModalPayload;

import java.util.Map;

/**
 * Клиентская модель компонента show-modal
 */
@Getter
@Setter
public class ShowModalPayload extends ModalPayload {
    @JsonProperty
    private String size;
    @JsonProperty
    private Boolean scrollable;
    @JsonProperty
    private Boolean closeButton = true;
    @JsonProperty
    private Boolean visible = true;
    @JsonProperty
    private Boolean prompt;
    @JsonProperty
    private Boolean hasHeader;
    @JsonProperty
    private String className;
    @JsonProperty
    private Object backdrop;
    @JsonProperty
    private Map<String, String> style;
}

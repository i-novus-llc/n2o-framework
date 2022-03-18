package net.n2oapp.framework.api.metadata.meta.action.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.List;

/**
 * Клиентская модель компонента действия alert
 */
@Getter
@Setter
public class AlertActionPayload implements ActionPayload {
    @JsonProperty
    private MessagePlacement key;
    @JsonProperty
    private List<ResponseMessage> alerts;
}

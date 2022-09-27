package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.List;

/**
 * Информация об уведомлениях после действия
 */
@Getter
@Setter
public class AlertSaga implements Compiled {
    @JsonProperty
    private String alertKey;
    @JsonProperty
    private List<ResponseMessage> messages;
}

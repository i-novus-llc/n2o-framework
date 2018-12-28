package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.Map;

/**
 * Сообщения после проверки данных
 */
@Getter
@Setter
public class MessageSaga implements Compiled {
    @JsonProperty
    private String form;
    @JsonProperty
    private Map<String, ResponseMessage> fields;
}

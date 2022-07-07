package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.List;
import java.util.Map;

/**
 * Ответ на запрос отправки данных
 */
@Getter
@Setter
public class SetDataResponse extends N2oResponse {
    @JsonProperty
    @JsonInclude
    private Map<String, Object> data;

    public SetDataResponse() {
    }

    public SetDataResponse(List<ResponseMessage> messages, String widgetId) {
        super(messages, widgetId);
    }
}

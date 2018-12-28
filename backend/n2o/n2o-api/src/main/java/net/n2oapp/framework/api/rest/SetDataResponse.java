package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.ui.RequestInfo;
import net.n2oapp.framework.api.ui.ResponseInfo;

import java.util.Map;

/**
 * Ответ на запрос отправки данных
 */
@Getter
@Setter
public class SetDataResponse extends N2oResponse {
    @JsonProperty
    private Map<String, Object> data;

    public SetDataResponse() {
    }

    public SetDataResponse(String messageForm) {
        setMessagesForm(messageForm);
    }

    public void addRedirect(RedirectSaga redirect) {
        if (getMeta() == null)
            setMeta(new MetaSaga());
        getMeta().setRedirect(redirect);
    }
}

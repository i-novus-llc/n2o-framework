package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.saga.AlertSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MessageSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Ответ на запросы N2O
 */
@Getter
@Setter
public class N2oResponse {
    /**
     * Мета информация
     */
    @JsonProperty
    private MetaSaga meta;
    private String messagesForm;
    private ResponseErrorInfo errorInfo;

    public void addResponseMessages(List<ResponseMessage> messageList) {
        if (messageList == null || messageList.isEmpty())
            return;
        messageList.forEach(this::addResponseMessage);
    }

    public void addResponseMessage(ResponseMessage message) {
        if (message == null) return;
        if (getMeta() == null)
            setMeta(new MetaSaga());
        if (message.getField() == null) {
            if (getMeta().getAlert() == null)
                getMeta().setAlert(new AlertSaga());
            if (getMeta().getAlert().getMessages() == null)
                getMeta().getAlert().setMessages(new ArrayList<>());
            getMeta().getAlert().getMessages().add(message);
            getMeta().getAlert().setAlertKey(messagesForm);
        } else {
            if (getMeta().getMessages() == null)
                getMeta().setMessages(new MessageSaga());
            if (getMeta().getMessages().getFields() == null)
                getMeta().getMessages().setFields(new HashMap<>());
            getMeta().getMessages().getFields().putIfAbsent(message.getField(), message);
        }
    }
}

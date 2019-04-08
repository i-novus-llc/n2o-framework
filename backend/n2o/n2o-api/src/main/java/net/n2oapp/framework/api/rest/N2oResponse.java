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
import java.util.Map;


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
    private int status = 200;

    public N2oResponse() {

    }

    public N2oResponse(List<ResponseMessage> messages, String widgetId) {
        meta = new MetaSaga();
        MessageSaga messageSaga = new MessageSaga();
        AlertSaga alert = new AlertSaga();
        Map<String, ResponseMessage> fieldMessages = new HashMap<>();
        List<ResponseMessage> widgetMessages = new ArrayList<>();
        for (ResponseMessage message : messages) {
            if (message.getField() != null) {
                fieldMessages.put(message.getField(), message);
            } else {
                widgetMessages.add(message);
            }
        }
        if (!widgetMessages.isEmpty()) {
            alert.setAlertKey(widgetId);
            alert.setMessages(widgetMessages);
            meta.setAlert(alert);
        }
        if (!fieldMessages.isEmpty()) {
            messageSaga.setFields(fieldMessages);
            messageSaga.setForm(widgetId);
            meta.setMessages(messageSaga);
        }
    }


    public void addResponseMessages(List<ResponseMessage> messageList, String widgetId) {
        if (messageList == null || messageList.isEmpty())
            return;
        messageList.forEach(m -> addResponseMessage(m, widgetId));
    }

    public void addResponseMessage(ResponseMessage message, String widgetId) {
        if (message == null) return;
        if (getMeta() == null)
            setMeta(new MetaSaga());
        if (message.getField() == null) {
            if (getMeta().getAlert() == null)
                getMeta().setAlert(new AlertSaga());
            if (getMeta().getAlert().getMessages() == null)
                getMeta().getAlert().setMessages(new ArrayList<>());
            getMeta().getAlert().getMessages().add(message);
            getMeta().getAlert().setAlertKey(widgetId);
        } else {
            if (getMeta().getMessages() == null)
                getMeta().setMessages(new MessageSaga());
            if (getMeta().getMessages().getFields() == null)
                getMeta().getMessages().setFields(new HashMap<>());
            getMeta().getMessages().getFields().putIfAbsent(message.getField(), message);
        }
    }

    public void setResponseMessages(List<ResponseMessage> messageList, String widgetId, Boolean stacked) {
        setMeta(new MetaSaga());

        if (messageList == null || messageList.isEmpty())
            return;

        if (messageList.stream().anyMatch(m -> m.getField() == null)) {
            getMeta().setAlert(new AlertSaga());
            getMeta().getAlert().setStacked(stacked);
        }
        messageList.forEach(m -> addResponseMessage(m, widgetId));
    }
}

package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.page.Dialog;
import net.n2oapp.framework.api.metadata.meta.saga.*;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Ответ на запросы N2O
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class N2oResponse {
    /**
     * Мета информация
     */
    @JsonProperty
    private MetaSaga meta;
    @JsonIgnore
    private int status = 200;

    public N2oResponse() {

    }

    public N2oResponse(List<ResponseMessage> messages, String widgetId) {
        addResponseMessages(messages, widgetId);
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
            if (getMeta().getMessages() == null) {
                MessageSaga messages = new MessageSaga();
                messages.setForm(widgetId);
                getMeta().setMessages(messages);
            }
            if (getMeta().getMessages().getFields() == null)
                getMeta().getMessages().setFields(new HashMap<>());
            getMeta().getMessages().getFields().putIfAbsent(message.getField(), message);
        }
    }

    public void setResponseMessages(List<ResponseMessage> messageList, String widgetId) {
        if (getMeta() == null)
            setMeta(new MetaSaga());
        getMeta().setMessages(null);

        if (messageList == null || messageList.isEmpty())
            return;

        if (messageList.stream().anyMatch(m -> m.getField() == null))
            getMeta().setAlert(new AlertSaga());
        messageList.forEach(m -> addResponseMessage(m, widgetId));
    }

    public void setDialog(Dialog dialog) {
        safeGetMeta().setDialog(dialog);
    }

    public void addRedirect(RedirectSaga redirect) {
        safeGetMeta().setRedirect(redirect);
    }

    public void addPolling(PollingSaga polling) {
        safeGetMeta().setPolling(polling);
    }

    public void addRefresh(RefreshSaga refresh) {
        safeGetMeta().setRefresh(refresh);
    }

    public void addLoading(LoadingSaga loading) {
        safeGetMeta().setLoading(loading);
    }

    public void addClear(String clearDatasource) {
        safeGetMeta().setClear(clearDatasource);
    }

    private MetaSaga safeGetMeta() {
        if (getMeta() == null)
            setMeta(new MetaSaga());
        return getMeta();
    }
}

package net.n2oapp.framework.api.ui;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.*;
import net.n2oapp.framework.api.metadata.meta.saga.AlertSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MessageSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;


/**
 * Сборка сообщения об ошибке в формате клиента
 */
public class ErrorMessageBuilder {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private MessageSourceAccessor messageSourceAccessor;

    public ErrorMessageBuilder(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    public ResponseMessage build(Exception e) {
        ResponseMessage resp = new ResponseMessage();
        resp.setText(buildText(e));
        if (!(e instanceof N2oUserException))
            resp.setStacktrace(getStackFrames(getStackTrace(e)));
        if (e instanceof N2oException) {
            resp.setChoice(((N2oException) e).getChoice());
            resp.setSeverityType(((N2oException) e).getSeverity());
            resp.setField(((N2oException) e).getField());
        } else {
            resp.setSeverityType(SeverityType.danger);
        }
        return resp;
    }

    public List<ResponseMessage> buildMessages(N2oValidationException e) {
        List<ResponseMessage> messages = new ArrayList<>();
        if (e.getMessages() != null) {
            for (ValidationMessage message : e.getMessages()) {
                ResponseMessage resp = new ResponseMessage();
                resp.setChoice(e.getChoice());
                resp.setSeverityType(e.getSeverity());
                resp.setField(message.getFieldId());
                resp.setText(message.getMessage());
                messages.add(resp);
            }
        }
        return messages;
    }

    private String getStackTrace(Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    private List<String> getStackFrames(String stacktrace) {
        StringTokenizer frames = new StringTokenizer(stacktrace, LINE_SEPARATOR);
        List<String> list = new ArrayList<>();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return list;
    }

    private String buildText(Exception e) {
        String message = "n2o.exceptions.error.message";
        String userMessage = e instanceof N2oException ? ((N2oException) e).getUserMessage() : null;
        message = userMessage != null ? userMessage : message;
        String localizedMessage = messageSourceAccessor.getMessage(message, message);
        if (e instanceof N2oException)
            return StringUtils.resolveLinks(localizedMessage, ((N2oException) e).getData());
        else
            return localizedMessage;
    }

    public MetaSaga buildMeta(Exception e, String failAlertWidgetId) {
        if (e == null) return null;
        MetaSaga meta = new MetaSaga();
        if (e instanceof N2oValidationException) {
            N2oValidationException exception = (N2oValidationException) e;
            List<ResponseMessage> responseMessages = buildMessages(exception);
            meta.setMessages(new MessageSaga());
            AlertSaga alert = new AlertSaga();
            HashMap<String, ResponseMessage> fields = new HashMap<>();
            List<ResponseMessage> responseMessagesForAlert = new ArrayList<>();
            for (ResponseMessage responseMessage : responseMessages) {
                if (responseMessage.getField() != null)
                    fields.put(responseMessage.getField(), responseMessage);
                else
                    responseMessagesForAlert.add(responseMessage);
            }
            if (!responseMessagesForAlert.isEmpty()) {
                alert.setAlertKey(failAlertWidgetId);
                alert.setMessages(responseMessagesForAlert);
            }
            meta.getMessages().setFields(fields);
            meta.getMessages().setForm(failAlertWidgetId);
            if (alert.getMessages() != null || alert.getAlertKey() != null) {
                meta.setAlert(alert);
            }
        } else if (e instanceof N2oException) {
            ResponseMessage responseMessage = build(e);
            meta.setAlert(new AlertSaga());
            meta.getAlert().setMessages(Collections.singletonList(responseMessage));
            meta.getAlert().setAlertKey(failAlertWidgetId);
        }
        return meta;
    }

}

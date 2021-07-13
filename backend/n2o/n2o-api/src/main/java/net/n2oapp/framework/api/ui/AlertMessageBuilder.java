package net.n2oapp.framework.api.ui;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Сборка сообщения об ошибке\успехе в формате клиента
 */
public class AlertMessageBuilder {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private MessageSourceAccessor messageSourceAccessor;
    private PropertyResolver propertyResolver;
    private Boolean showStacktrace = true;

    public AlertMessageBuilder(MessageSourceAccessor messageSourceAccessor, PropertyResolver propertyResolver) {
        this.messageSourceAccessor = messageSourceAccessor;
        this.propertyResolver = propertyResolver;
    }

    public AlertMessageBuilder(MessageSourceAccessor messageSourceAccessor, PropertyResolver propertyResolver,
                               Boolean showStacktrace) {
        this.messageSourceAccessor = messageSourceAccessor;
        this.propertyResolver = propertyResolver;
        this.showStacktrace = showStacktrace;
    }

    public ResponseMessage build(Exception e) {
        ResponseMessage resp = constructMessage();
        return prepareMessage(e, resp);
    }

    public ResponseMessage build(Exception e, RequestInfo requestInfo) {
        ResponseMessage resp = constructMessage(requestInfo);
        return prepareMessage(e, resp);
    }

    private ResponseMessage prepareMessage(Exception e, ResponseMessage resp) {
        resp.setText(buildText(e));

        if (showStacktrace && !(e instanceof N2oUserException))
            resp.setStacktrace(getStackFrames(getStackTrace(e)));
        if (e instanceof N2oException) {
            resp.setSeverityType(((N2oException) e).getSeverity());
            resp.setField(((N2oException) e).getField());
        } else {
            resp.setSeverityType(SeverityType.danger);
        }
        return resp;
    }

    public ResponseMessage buildSuccessMessage(String successText, RequestInfo requestInfo, DataSet data) {
        ResponseMessage message = constructMessage(requestInfo);
        message.setSeverityType(SeverityType.success);
        message.setText(StringUtils.resolveLinks(successText, data));
        message.setData(data);
        return message;
    }

    private ResponseMessage constructMessage() {
        ResponseMessage message = new ResponseMessage();
        if (propertyResolver != null) {
            message.setPosition(propertyResolver.getProperty("n2o.api.message.position"));
            message.setPlacement(propertyResolver.getProperty("n2o.api.message.placement"));
        }
        return message;
    }

    public ResponseMessage constructMessage(RequestInfo requestInfo) {
        ResponseMessage message = constructMessage();
        if (requestInfo.getMessagePosition() != null)
            message.setPosition(requestInfo.getMessagePosition().name());
        if (requestInfo.getMessagePlacement() != null)
            message.setPlacement(requestInfo.getMessagePlacement().name());
        return message;
    }

    private List<ResponseMessage> buildValidationMessages(N2oValidationException e, RequestInfo requestInfo) {
        List<ResponseMessage> messages = new ArrayList<>();
        if (e.getMessages() != null) {
            for (ValidationMessage message : e.getMessages()) {
                ResponseMessage resp = constructMessage(requestInfo);
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

    public List<ResponseMessage> buildMessages(Exception e, RequestInfo requestInfo) {
        return e instanceof N2oValidationException
                ? buildValidationMessages((N2oValidationException) e, requestInfo)
                : Collections.singletonList(build(e, requestInfo));
    }

}

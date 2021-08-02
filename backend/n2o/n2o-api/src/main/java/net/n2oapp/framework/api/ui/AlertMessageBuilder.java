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
        ResponseMessage resp = constructMessage(getExceptionSeverity(e));
        return prepareMessage(e, resp);
    }

    public ResponseMessage build(Exception e, RequestInfo requestInfo) {
        ResponseMessage resp = buildMessage(requestInfo, getExceptionSeverity(e));
        return prepareMessage(e, resp);
    }

    public ResponseMessage buildMessage(RequestInfo requestInfo, SeverityType severityType) {
        ResponseMessage message = constructMessage(severityType);
        if (requestInfo.getMessagePosition() != null)
            message.setPosition(requestInfo.getMessagePosition().name());
        if (requestInfo.getMessagePlacement() != null)
            message.setPlacement(requestInfo.getMessagePlacement().name());
        return message;
    }

    public List<ResponseMessage> buildMessages(Exception e, RequestInfo requestInfo) {
        return e instanceof N2oValidationException
                ? buildValidationMessages((N2oValidationException) e, requestInfo)
                : Collections.singletonList(build(e, requestInfo));
    }

    public ResponseMessage buildSuccessMessage(String successText, RequestInfo requestInfo, DataSet data) {
        ResponseMessage message = buildMessage(requestInfo, SeverityType.success);
        message.setText(StringUtils.resolveLinks(successText, data));
        message.setData(data);
        return message;
    }

    private SeverityType getExceptionSeverity(Exception e) {
        return e instanceof N2oException ? ((N2oException) e).getSeverity() : SeverityType.danger;
    }

    private ResponseMessage prepareMessage(Exception e, ResponseMessage resp) {
        resp.setText(buildText(e));

        if (showStacktrace && !(e instanceof N2oUserException))
            resp.setStacktrace(getStackFrames(getStackTrace(e)));
        if (e instanceof N2oException)
            resp.setField(((N2oException) e).getField());
        return resp;
    }

    private ResponseMessage constructMessage(SeverityType severityType) {
        ResponseMessage message = new ResponseMessage();
        message.setSeverityType(severityType);
        if (propertyResolver != null) {
            message.setPosition(propertyResolver.getProperty("n2o.api.message.position"));
            message.setPlacement(propertyResolver.getProperty("n2o.api.message.placement"));
            if (severityType != null) {
                Integer timeout = Integer.parseInt(
                        propertyResolver.getProperty(String.format("n2o.api.message.%s.timeout", severityType.getId())));
                message.setTimeout(timeout);
            }
        }
        return message;
    }

    private List<ResponseMessage> buildValidationMessages(N2oValidationException e, RequestInfo requestInfo) {
        List<ResponseMessage> messages = new ArrayList<>();
        if (e.getMessages() != null) {
            for (ValidationMessage message : e.getMessages()) {
                ResponseMessage resp = buildMessage(requestInfo, e.getSeverity());
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

}

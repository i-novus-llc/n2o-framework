package net.n2oapp.framework.api.ui;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.exception.N2oQueryExecutionException;
import net.n2oapp.framework.api.exception.*;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;


/**
 * Сборка сообщения об ошибке\успехе в формате клиента
 */
public class AlertMessageBuilder {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private MessageSourceAccessor messageSourceAccessor;
    private PropertyResolver propertyResolver;
    private boolean showStacktrace = true;
    private boolean devMode;

    public AlertMessageBuilder(MessageSourceAccessor messageSourceAccessor, PropertyResolver propertyResolver) {
        this.messageSourceAccessor = messageSourceAccessor;
        this.propertyResolver = propertyResolver;
    }

    public AlertMessageBuilder(MessageSourceAccessor messageSourceAccessor, PropertyResolver propertyResolver,
                               boolean showStacktrace) {
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

    public ResponseMessage buildMessage(RequestInfo requestInfo, SeverityTypeEnum severityType) {
        ResponseMessage message = constructMessage(severityType);
        if (requestInfo.getMessagePlacement() != null)
            message.setPlacement(MessagePlacementEnum.valueOf(requestInfo.getMessagePlacement().name()));
        return message;
    }

    public List<ResponseMessage> buildMessages(Exception e, RequestInfo requestInfo) {
        return e instanceof N2oValidationException ve
                ? buildValidationMessages(ve, requestInfo)
                : Collections.singletonList(build(e, requestInfo));
    }

    public ResponseMessage buildSuccessMessage(ActionRequestInfo<DataSet> requestInfo, DataSet data) {
        ResponseMessage message = buildMessage(requestInfo, SeverityTypeEnum.success);
        message.setText(StringUtils.resolveLinks(requestInfo.getOperation().getSuccessText(), data));
        message.setTitle(StringUtils.resolveLinks(requestInfo.getOperation().getSuccessTitle(), data));
        return message;
    }

    private void initDevMode(PropertyResolver propertyResolver) {
        Boolean activeDevMode = propertyResolver != null ? propertyResolver.getProperty("n2o.ui.message.dev-mode", Boolean.class) : null;
        this.devMode = activeDevMode != null && activeDevMode;
    }

    private SeverityTypeEnum getExceptionSeverity(Exception e) {
        return e instanceof N2oException n2oException ? n2oException.getSeverity() : SeverityTypeEnum.danger;
    }

    private ResponseMessage prepareMessage(Exception e, ResponseMessage resp) {
        initDevMode(propertyResolver);
        resp.setText(buildText(e));

        if (!devMode && e instanceof N2oException n2oException && n2oException.getUserMessageTitle() != null)
            resp.setTitle(n2oException.getUserMessageTitle());

        if (showStacktrace && !(e instanceof N2oUserException))
            resp.setPayload(initPayload(e));
        if (e instanceof N2oException n2oException)
            resp.setField(n2oException.getField());
        return resp;
    }

    private List<String> initPayload(Exception e) {
        if (devMode && e instanceof N2oQueryExecutionException n2oQueryExecutionException)
            return Collections.singletonList("Executed query: " + n2oQueryExecutionException.getQuery());
        return getStackFrames(getStackTrace(e));
    }

    private ResponseMessage constructMessage(SeverityTypeEnum severityType) {
        ResponseMessage message = new ResponseMessage();
        message.setSeverityType(severityType);
        if (propertyResolver != null) {
            message.setPlacement(propertyResolver.getProperty("n2o.api.message.placement", MessagePlacementEnum.class));
            if (severityType != null) {
                Integer timeout = Integer.parseInt(
                        propertyResolver.getProperty(String.format("n2o.api.message.%s.timeout", severityType.getId())));
                message.setTimeout(timeout);
            }
        }
        return message;
    }

    public List<ResponseMessage> buildValidationMessages(N2oValidationException e, RequestInfo requestInfo) {
        List<ResponseMessage> messages = new ArrayList<>();
        if (e.getMessages() != null) {
            for (ValidationMessage message : e.getMessages()) {
                ResponseMessage resp = buildMessage(requestInfo, e.getSeverity());
                resp.setField(message.getFieldId());
                resp.setText(message.getMessage());
                resp.setTitle(message.getMessageTitle());
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
        String userMessage = initUserMessage(e);
        message = userMessage != null ? userMessage : message;
        String localizedMessage = messageSourceAccessor.getMessage(message, message);
        if (e instanceof N2oException n2oException)
            return StringUtils.resolveLinks(localizedMessage, n2oException.getData());
        else
            return localizedMessage;
    }

    private String initUserMessage(Exception e) {
        if (devMode && !(e instanceof N2oUserException))
            return e.getMessage();
        if (e instanceof N2oException n2oException)
            return n2oException.getUserMessage();
        return null;
    }
}

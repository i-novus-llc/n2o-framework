package net.n2oapp.framework.boot.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.QueryExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.engine.data.N2oOperationExceptionHandler;
import net.n2oapp.platform.i18n.Messages;
import net.n2oapp.platform.i18n.UserException;
import net.n2oapp.platform.jaxrs.RestException;
import net.n2oapp.platform.jaxrs.RestMessage;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

/**
 * Получение пользовательских сообщений и стектрейса ошибок от REST сервисов платформы N2O Boot
 */
@Setter
public class PlatformExceptionHandler extends N2oOperationExceptionHandler implements QueryExceptionHandler {
    private Messages messages;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public N2oException handle(CompiledObject.Operation operation, DataSet dataSet, Exception e) {
        N2oException exception = handle(e);
        if (exception != null)
            return exception;
        return super.handle(operation, dataSet, e);
    }

    @Override
    public N2oException handle(CompiledQuery compiledQuery, N2oPreparedCriteria n2oPreparedCriteria, Exception e) {
        N2oException exception = handle(e);
        if (exception != null)
            return exception;
        if (e instanceof N2oException n2oException)
            return n2oException;
        return new N2oException(e);
    }

    private N2oException handle(Exception e) {
        N2oException handled = handleJaxRsException(e);
        if (handled != null)
            return handled;
        handled = handleRestClientException(e);
        if (handled != null)
            return handled;
        return handleUserException(e);
    }

    private N2oException handleJaxRsException(Exception e) {
        RestException jaxRsException = unwrapEx(e, RestException.class);
        if (jaxRsException != null) {
            if (jaxRsException.getResponseStatus() >= 400 && jaxRsException.getResponseStatus() < 500) {
                return new N2oUserException(jaxRsException.getMessage());
            } else {
                return new N2oException(jaxRsException);
            }
        }
        return null;
    }

    private N2oException handleUserException(Exception e) {
        UserException userException = unwrapEx(e, UserException.class);
        return (userException != null) ? new N2oUserException(getMessage(userException)) : null;
    }

    private String getMessage(UserException userException) {
        if (messages != null) {
            return userException.getArgs() == null
                    ? messages.getMessage(userException.getMessage())
                    : messages.getMessage(userException.getMessage(), userException.getArgs());
        }

        return userException.getMessage();
    }

    private N2oException handleRestClientException(Exception e) {
        HttpStatusCodeException restClientException = unwrapEx(e, HttpStatusCodeException.class);
        if (restClientException != null) {
            RestMessage message;
            try {
                message = objectMapper.readValue(restClientException.getResponseBodyAsByteArray(), RestMessage.class);
            } catch (IOException e1) {
                N2oException n2oException = new N2oException(e);
                n2oException.addSuppressed(e1);
                return n2oException;
            }
            if (restClientException.getStatusCode().is4xxClientError() && message != null) {
                return new N2oUserException(message.getMessage());
            } else if (restClientException.getStatusCode().is5xxServerError() && message != null) {
                return new N2oException(new RestException(message, restClientException.getStatusCode().value()));
            }
        }
        return null;
    }

    private <T extends Exception> T unwrapEx(Throwable e, Class<T> exClass) {
        if (exClass.isAssignableFrom(e.getClass())) {
            return exClass.cast(e);
        } else if (e.getCause() != null) {
            return unwrapEx(e.getCause(), exClass);
        } else
            return null;
    }
}

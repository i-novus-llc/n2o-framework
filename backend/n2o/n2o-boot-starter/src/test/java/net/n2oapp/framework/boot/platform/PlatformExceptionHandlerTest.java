package net.n2oapp.framework.boot.platform;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.platform.i18n.Message;
import net.n2oapp.platform.i18n.Messages;
import net.n2oapp.platform.i18n.UserException;
import net.n2oapp.platform.jaxrs.RestException;
import net.n2oapp.platform.jaxrs.RestMessage;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тесты класса {@link PlatformExceptionHandler}
 */
class PlatformExceptionHandlerTest {
    /**
     * Тест обработки пользовательских сообщений из исключений выбрасываемых RestTemplate
     */
    @Test
    void handleUserMessageFromRestTemplate() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        String body = "{\"message\":\"Wrong data\"}";
        N2oException e = handler.handle((CompiledObject.Operation) null, null,
                HttpClientErrorException.create(HttpStatus.BAD_REQUEST,
                        "Bad request",
                        HttpHeaders.EMPTY,
                        body.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8));
        assertThat(e, instanceOf(N2oUserException.class));
        N2oUserException userException = (N2oUserException) e;
        assertThat(userException.getUserMessage(), is("Wrong data"));
    }

    /**
     * Тест обработки пользовательских сообщений из исключений выбрасываемых JaxRs Proxy Client
     */
    @Test
    void handleUserMessageFromJaxRsClient() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        N2oException e = handler.handle((CompiledQuery) null, null,
                new RestException(new RestMessage("Wrong data"), 400));
        assertThat(e, instanceOf(N2oUserException.class));
        N2oUserException userException = (N2oUserException) e;
        assertThat(userException.getUserMessage(), is("Wrong data"));
    }

    /**
     * Тест обработки пользовательских сообщений из исключений JaxRs Proxy Client,
     * обёрнутых провайдером данных в другое исключение
     */
    @Test
    void handleUserMessageFromWrappedJaxRsClient() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        N2oException e = handler.handle(new CompiledObject.Operation(), null,
                new N2oException(new RestException(new RestMessage("Wrong data"), 400)));
        assertThat(e, instanceOf(N2oUserException.class));
        N2oUserException userException = (N2oUserException) e;
        assertThat(userException.getUserMessage(), is("Wrong data"));
    }

    /**
     * Тест обработки пользовательских сообщений из исключений UserException
     */
    @Test
    void handleUserMessageFromUserException() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        N2oException e = handler.handle((CompiledQuery) null, null,
                new UserException("Wrong data"));
        assertThat(e, instanceOf(N2oUserException.class));
        N2oUserException userException = (N2oUserException) e;
        assertThat(userException.getUserMessage(), is("Wrong data"));
    }

    /**
     * Тест локализации сообщений из исключений UserException
     */
    @Test
    void handleLocalizedUserMessageFromUserException() {
        Messages messages = mock(Messages.class);
        when(messages.getMessage("wrong.data")).thenReturn("Неверные данные");
        when(messages.getMessage("wrong.field", "name")).thenReturn("Неверное поле name");
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        handler.setMessages(messages);

        N2oException e = handler.handle((CompiledQuery) null, null, new UserException("wrong.data"));
        assertThat(e, instanceOf(N2oUserException.class));
        assertThat(((N2oUserException) e).getUserMessage(), is("Неверные данные"));

        e = handler.handle((CompiledObject.Operation) null, null,
                new UserException(new Message("wrong.field", "name")));
        assertThat(e, instanceOf(N2oUserException.class));
        assertThat(((N2oUserException) e).getUserMessage(), is("Неверное поле name"));
    }

    /**
     * Тест обработки технических исключений выбрасываемых RestTemplate
     */
    @Test
    void handleStacktraceFromRestTemplate() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        String body = "{\"message\":\"Unexpected error\",\"stackTrace\":" +
                "[\"com.example.test.BusinessException: something went wrong\"," +
                "\"\\tat com.example.test.BusinessService.someMethod(BusinessService.java:123)\"]}";
        N2oException e = handler.handle((CompiledQuery) null, null,
                HttpClientErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Bad request",
                        HttpHeaders.EMPTY,
                        body.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8));

        assertThat(e, not(instanceOf(N2oUserException.class)));
        assertThat(e.getCause(), instanceOf(RestException.class));
        assertThat(e.getMessage(), containsString("Unexpected error"));
        assertThat(hasRemoteStackFrame(e.getCause(), "something went wrong"), is(true));
    }

    /**
     * Тест обработки технических исключений выбрасываемых JaxRs Proxy Client
     */
    @Test
    void handleStacktraceFromJaxRsClient() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        RestMessage message = new RestMessage("Unexpected error");
        message.setStackTrace(new String[]{
                "com.example.test.BusinessException: something went wrong",
                "\tat com.example.test.BusinessService.someMethod(BusinessService.java:123)"});
        RestException target = new RestException(message, 500);
        //
        N2oException e = handler.handle((CompiledObject.Operation) null, null, target);
        assertThat(e, not(instanceOf(N2oUserException.class)));
        assertThat(e.getCause(), instanceOf(RestException.class));
        assertThat(e.getMessage(), containsString("Unexpected error"));
        assertThat(hasRemoteStackFrame(e.getCause(), "something went wrong"), is(true));
    }

    /**
     * Тест обработки ответа RestTemplate, тело которого не является сообщением платформы
     */
    @Test
    void handleUnreadableBodyFromRestTemplate() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        HttpClientErrorException target = HttpClientErrorException.create(HttpStatus.BAD_REQUEST,
                "Bad request",
                HttpHeaders.EMPTY,
                "<html>Bad request</html>".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8);
        N2oException e = handler.handle((CompiledQuery) null, null, target);
        assertThat(e, not(instanceOf(N2oUserException.class)));
        assertThat(e.getCause(), sameInstance(target));
        assertThat(e.getSuppressed().length, is(1));
    }

    /**
     * Тест обработки исключений, не относящихся к платформе:
     * используется поведение N2O по умолчанию
     */
    @Test
    void handleNonPlatformException() {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        IllegalStateException target = new IllegalStateException("Something went wrong");

        CompiledObject.Operation operation = new CompiledObject.Operation();
        operation.setFailText("Не удалось сохранить");
        N2oException e = handler.handle(operation, null, target);
        assertThat(e, not(instanceOf(N2oUserException.class)));
        assertThat(e.getCause(), sameInstance(target));
        assertThat(e.getUserMessage(), is("Не удалось сохранить"));

        e = handler.handle((CompiledQuery) null, null, target);
        assertThat(e, not(instanceOf(N2oUserException.class)));
        assertThat(e.getCause(), sameInstance(target));

        N2oUserException n2oUserException = new N2oUserException("Wrong data");
        assertThat(handler.handle((CompiledQuery) null, null, n2oUserException), sameInstance(n2oUserException));
        assertThat(handler.handle(operation, null, n2oUserException), sameInstance(n2oUserException));
    }

    private static boolean hasRemoteStackFrame(Throwable e, String text) {
        return Stream.iterate(e, t -> t != null, Throwable::getCause)
                .anyMatch(t -> t.toString().contains(text)
                        || Stream.of(t.getStackTrace()).anyMatch(sf -> sf.toString().contains(text)));
    }
}

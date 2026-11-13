package net.n2oapp.framework.boot.platform;

import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.data.QueryExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.platform.i18n.Messages;
import net.n2oapp.platform.i18n.UserException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тесты класса {@link N2oBootAutoConfiguration}
 */
class N2oBootAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(N2oBootAutoConfiguration.class));

    /**
     * Обработчик платформы подключается для операций и для выборок
     */
    @Test
    void registersOperationAndQueryExceptionHandler() {
        contextRunner.run(context -> {
            OperationExceptionHandler operationHandler = context.getBean(OperationExceptionHandler.class);
            QueryExceptionHandler queryHandler = context.getBean(QueryExceptionHandler.class);

            N2oException e = operationHandler.handle(null, null, new UserException("Wrong data"));
            assertThat(e, instanceOf(N2oUserException.class));
            assertThat(e.getUserMessage(), is("Wrong data"));

            e = queryHandler.handle(null, null, new UserException("Wrong data"));
            assertThat(e, instanceOf(N2oUserException.class));
            assertThat(e.getUserMessage(), is("Wrong data"));
        });
    }

    /**
     * Обработчик операций, объявленный в приложении, не заменяется,
     * при этом обработчик выборок платформы остаётся подключённым
     */
    @Test
    void backsOffIfOperationHandlerDefined() {
        OperationExceptionHandler custom = (o, data, e) -> new N2oException("custom");
        contextRunner.withBean(OperationExceptionHandler.class, () -> custom).run(context -> {
            assertThat(context.getBean(OperationExceptionHandler.class), sameInstance(custom));
            N2oException e = context.getBean(QueryExceptionHandler.class)
                    .handle(null, null, new UserException("Wrong data"));
            assertThat(e, instanceOf(N2oUserException.class));
        });
    }

    /**
     * Обработчик выборок, объявленный в приложении, не заменяется,
     * при этом обработчик операций платформы остаётся подключённым
     */
    @Test
    void backsOffIfQueryHandlerDefined() {
        QueryExceptionHandler custom = (query, criteria, e) -> new N2oException("custom");
        contextRunner.withBean(QueryExceptionHandler.class, () -> custom).run(context -> {
            assertThat(context.getBean(QueryExceptionHandler.class), sameInstance(custom));
            N2oException e = context.getBean(OperationExceptionHandler.class)
                    .handle(null, null, new UserException("Wrong data"));
            assertThat(e, instanceOf(N2oUserException.class));
        });
    }

    /**
     * При наличии бина Messages сообщения UserException локализуются
     */
    @Test
    void usesMessagesIfAvailable() {
        Messages messages = mock(Messages.class);
        when(messages.getMessage("wrong.data")).thenReturn("Неверные данные");
        contextRunner.withBean(Messages.class, () -> messages).run(context -> {
            N2oException e = context.getBean(QueryExceptionHandler.class)
                    .handle(null, null, new UserException("wrong.data"));
            assertThat(e.getUserMessage(), is("Неверные данные"));
        });
    }

    /**
     * Обработчик, объявленный в приложении и реализующий оба интерфейса, не заменяется
     */
    @Test
    void backsOffIfHandlerDefined() {
        PlatformExceptionHandler custom = new PlatformExceptionHandler();
        contextRunner.withBean("customHandler", PlatformExceptionHandler.class, () -> custom).run(context -> {
            assertThat(context.getBean(OperationExceptionHandler.class), sameInstance(custom));
            assertThat(context.getBean(QueryExceptionHandler.class), sameInstance(custom));
        });
    }
}

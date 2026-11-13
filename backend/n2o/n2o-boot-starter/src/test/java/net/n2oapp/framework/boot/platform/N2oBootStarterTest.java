package net.n2oapp.framework.boot.platform;

import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.data.QueryExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.engine.data.N2oOperationExceptionHandler;
import net.n2oapp.framework.engine.data.N2oQueryExceptionHandler;
import net.n2oapp.platform.i18n.UserException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

/**
 * Проверка, что в приложении N2O обработчики ошибок по умолчанию заменяются обработчиком платформы
 */
@SpringBootTest(classes = N2oBootStarterTest.class)
@EnableAutoConfiguration
class N2oBootStarterTest {

    @Autowired
    private OperationExceptionHandler operationExceptionHandler;

    @Autowired
    private QueryExceptionHandler queryExceptionHandler;

    @Test
    void platformHandlerReplacesN2oDefaults() {
        assertThat(operationExceptionHandler, not(instanceOf(N2oOperationExceptionHandler.class)));
        assertThat(queryExceptionHandler, not(instanceOf(N2oQueryExceptionHandler.class)));

        N2oException e = operationExceptionHandler.handle(null, null, new UserException("Wrong data"));
        assertThat(e, instanceOf(N2oUserException.class));
        assertThat(e.getUserMessage(), is("Wrong data"));

        e = queryExceptionHandler.handle(null, null, new UserException("Wrong data"));
        assertThat(e, instanceOf(N2oUserException.class));
        assertThat(e.getUserMessage(), is("Wrong data"));
    }
}

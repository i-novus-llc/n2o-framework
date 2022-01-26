package net.n2oapp.framework.api.ui;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import org.junit.Test;
import org.springframework.context.support.MessageSourceAccessor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тесты {@link AlertMessageBuilder}
 */
public class FieldAlertMessageBuilderTest {
    @Test
    public void testAnyException() {
        MessageSourceAccessor messageSource = mock(MessageSourceAccessor.class);
        when(messageSource.getMessage("n2o.exceptions.error.message", "n2o.exceptions.error.message")).thenReturn("Internal error");
        AlertMessageBuilder builder = new AlertMessageBuilder(messageSource, null);
        Exception e = new IllegalStateException();
        ResponseMessage message = builder.build(e);
        assertThat(message.getText(), is("Internal error"));
        assertThat(message.getColor(), is("danger"));
        assertThat(message.getStacktrace(), hasItem(containsString("AlertMessageBuilderTest")));
        builder = new AlertMessageBuilder(messageSource, null, false);
        message = builder.build(e);
        assertThat(message.getStacktrace(), nullValue());
    }

    @Test
    public void testUserException() {
        MessageSourceAccessor messageSource = mock(MessageSourceAccessor.class);
        when(messageSource.getMessage("user.code", "user.code")).thenReturn("User message {0}");
        AlertMessageBuilder builder = new AlertMessageBuilder(messageSource, null);
        N2oException e = new N2oUserException("user.code").addData("test");
        ResponseMessage message = builder.build(e);
        assertThat(message.getText(), is("User message test"));
    }
}

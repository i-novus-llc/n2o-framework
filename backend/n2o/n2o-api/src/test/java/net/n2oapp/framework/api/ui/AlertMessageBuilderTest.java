package net.n2oapp.framework.api.ui;

import net.n2oapp.framework.api.data.exception.N2oQueryExecutionException;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.junit.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тесты {@link AlertMessageBuilder}
 */
public class AlertMessageBuilderTest {
    @Test
    public void testAnyException() {
        MessageSourceAccessor messageSource = mock(MessageSourceAccessor.class);
        when(messageSource.getMessage("n2o.exceptions.error.message", "n2o.exceptions.error.message")).thenReturn("Internal error");
        AlertMessageBuilder builder = new AlertMessageBuilder(messageSource, null);
        Exception e = new IllegalStateException();
        ResponseMessage message = builder.build(e);
        assertThat(message.getText(), is("Internal error"));
        assertThat(message.getSeverity(), is("danger"));
        assertThat(message.getPayload(), hasItem(containsString("AlertMessageBuilderTest")));
        builder = new AlertMessageBuilder(messageSource, null, false);
        message = builder.build(e);
        assertThat(message.getPayload(), nullValue());
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

    @Test
    public void testDevMode() {
        MessageSourceAccessor messageSource = mock(MessageSourceAccessor.class);
        when(messageSource.getMessage("Unspecified label for dropdown-menu", "Unspecified label for dropdown-menu"))
                .thenReturn("Unspecified label for dropdown-menu");
        PropertyResolver propertyResolver = mock(PropertyResolver.class);
        when(propertyResolver.getProperty("n2o.ui.message.dev-mode", Boolean.class)).thenReturn(true);
        when(propertyResolver.getProperty("n2o.api.message.danger.timeout")).thenReturn("8000");
        AlertMessageBuilder builder = new AlertMessageBuilder(messageSource, propertyResolver);
        N2oException e = new N2oMetadataValidationException("Unspecified label for dropdown-menu");

        ResponseMessage message = builder.build(e);
        assertThat(message.getText(), is("Unspecified label for dropdown-menu"));
        assertThat(message.getPayload(), notNullValue());
    }

    @Test
    public void testQueryExecutionExceptionInDevMode() {
        MessageSourceAccessor messageSource = mock(MessageSourceAccessor.class);
        String message = "couldn't rewrite query getCar";
        String query = "query MyQuery {getCar(id: )}";
        when(messageSource.getMessage(message, message)).thenReturn(message);
        PropertyResolver propertyResolver = mock(PropertyResolver.class);
        when(propertyResolver.getProperty("n2o.ui.message.dev-mode", Boolean.class)).thenReturn(true);
        when(propertyResolver.getProperty("n2o.api.message.danger.timeout")).thenReturn("8000");
        AlertMessageBuilder builder = new AlertMessageBuilder(messageSource, propertyResolver);
        N2oQueryExecutionException e = new N2oQueryExecutionException(message, query);

        ResponseMessage responseMessage = builder.build(e);
        assertThat(responseMessage.getText(), is(message));
        assertThat(responseMessage.getPayload().size(), is(1));
        assertThat(responseMessage.getPayload().get(0), is("Executed query: " + query));
    }
}

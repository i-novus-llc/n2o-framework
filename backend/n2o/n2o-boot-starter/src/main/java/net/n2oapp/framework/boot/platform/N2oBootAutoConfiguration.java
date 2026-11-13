package net.n2oapp.framework.boot.platform;

import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.data.QueryExceptionHandler;
import net.n2oapp.framework.boot.N2oFrameworkAutoConfiguration;
import net.n2oapp.platform.i18n.Messages;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Автоконфигурация интеграции N2O Framework с платформой N2O Boot.
 */
@AutoConfiguration(before = N2oFrameworkAutoConfiguration.class)
public class N2oBootAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OperationExceptionHandler.class)
    public OperationExceptionHandler platformOperationExceptionHandler(ObjectProvider<Messages> messages) {
        return createHandler(messages)::handle;
    }

    @Bean
    @ConditionalOnMissingBean(QueryExceptionHandler.class)
    public QueryExceptionHandler platformQueryExceptionHandler(ObjectProvider<Messages> messages) {
        return createHandler(messages)::handle;
    }

    private PlatformExceptionHandler createHandler(ObjectProvider<Messages> messages) {
        PlatformExceptionHandler handler = new PlatformExceptionHandler();
        messages.ifAvailable(handler::setMessages);
        return handler;
    }
}

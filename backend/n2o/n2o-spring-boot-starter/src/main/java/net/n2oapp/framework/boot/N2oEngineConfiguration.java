package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.engine.data.*;
import net.n2oapp.framework.engine.data.java.JavaDataProviderEngine;
import net.n2oapp.framework.engine.data.java.ObjectLocator;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.engine.modules.stack.SpringDataProcessingStack;
import net.n2oapp.framework.engine.validation.N2oValidationModule;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Конфигурация провайдеров данных
 */
@Configuration
@ComponentScan(basePackages = "net.n2oapp.framework.engine", lazyInit = true)
public class N2oEngineConfiguration {

    @Value("${n2o.engine.pageStartsWith0}")
    private boolean pageStartsWith0;

    @Bean
    @ConditionalOnMissingBean
    public DataProcessingStack dataProcessingStack(ApplicationContext context) {
        SpringDataProcessingStack dataProcessingStack = new SpringDataProcessingStack();
        dataProcessingStack.setApplicationContext(context);
        return dataProcessingStack;
    }

    @Bean
    @ConditionalOnMissingBean
    public N2oInvocationFactory actionInvocationFactory(ApplicationContext context) {
        N2oInvocationFactory actionInvocationFactory = new N2oInvocationFactory();
        actionInvocationFactory.setApplicationContext(context);
        return actionInvocationFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextProcessor contextProcessor(Context context) {
        return new ContextProcessor(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public InvocationProcessor invocationProcessor(N2oInvocationFactory invocationFactory,
                                                   ContextProcessor contextProcessor,
                                                   DomainProcessor domainProcessor) {
        return new N2oInvocationProcessor(invocationFactory, contextProcessor, domainProcessor);
    }

    @Bean
    public ValidationProcessor validationProcessor(InvocationProcessor invocationProcessor) {
        return new ValidationProcessor(invocationProcessor);
    }

    @Bean
    public N2oValidationModule validationModule(ValidationProcessor processor) {
        return new N2oValidationModule(processor);
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryProcessor queryProcessor(N2oInvocationFactory invocationFactory,
                                         ContextProcessor contextProcessor,
                                         DomainProcessor domainProcessor) {
        N2oQueryProcessor n2oQueryProcessor = new N2oQueryProcessor(invocationFactory, contextProcessor,
                domainProcessor);
        n2oQueryProcessor.setCriteriaResolver(new N2oCriteriaConstructor(pageStartsWith0));
        n2oQueryProcessor.setPageStartsWith0(pageStartsWith0);
        return n2oQueryProcessor;
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationExceptionHandler operationExceptionHandler() {
        return new N2oOperationExceptionHandler();
    }

    @Bean
    public N2oOperationProcessor actionProcessor(InvocationProcessor invocationProcessor,
                                                 OperationExceptionHandler operationExceptionHandler) {
        return new N2oOperationProcessor(invocationProcessor, operationExceptionHandler);
    }

    @Bean
    public JavaDataProviderEngine javaDataProviderEngine(Optional<List<ObjectLocator>> locators) {
        JavaDataProviderEngine javaDataProviderEngine = new JavaDataProviderEngine();
        javaDataProviderEngine.setLocators(locators.orElse(Collections.EMPTY_LIST));
        return javaDataProviderEngine;
    }

}

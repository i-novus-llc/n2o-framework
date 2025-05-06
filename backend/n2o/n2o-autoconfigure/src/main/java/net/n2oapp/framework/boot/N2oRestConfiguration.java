package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.ui.N2oAlertMessagesConstructor;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.framework.ui.controller.action.SetController;
import net.n2oapp.framework.ui.controller.action.ValidationController;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import net.n2oapp.framework.ui.controller.query.GetController;
import net.n2oapp.framework.ui.controller.query.MergeValuesController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация контроллеров
 */
@AutoConfiguration
@ServletComponentScan("net.n2oapp.framework")
@ComponentScan(basePackages = "net.n2oapp.framework.ui", lazyInit = true)
public class N2oRestConfiguration {

    @Value("${n2o.ui.message.stacktrace:true}")
    private Boolean showStacktrace;


    @Bean
    ControllerFactory controllerFactory(Map<String, SetController> setControllers,
                                        Map<String, GetController> getControllers,
                                        Map<String, ValidationController> validationControllers) {
        Map<String, Object> controllers = new HashMap<>();
        controllers.putAll(setControllers);
        controllers.putAll(getControllers);
        controllers.putAll(validationControllers);
        return new N2oControllerFactory(controllers);
    }

    @Bean
    QueryController queryController(DataProcessingStack dataProcessingStack,
                                    QueryProcessor queryProcessor,
                                    SubModelsProcessor subModelsProcessor,
                                    AlertMessageBuilder messageBuilder,
                                    AlertMessagesConstructor messagesConstructor) {
        return new QueryController(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder, messagesConstructor);
    }

    @Bean
    MergeValuesController mergeValuesController(DataProcessingStack dataProcessingStack,
                                                QueryProcessor queryProcessor,
                                                SubModelsProcessor subModelsProcessor,
                                                AlertMessageBuilder messageBuilder) {
        return new MergeValuesController(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder);
    }

    @Bean
    public OperationController operationController(DataProcessingStack dataProcessingStack,
                                                   N2oOperationProcessor operationProcessor,
                                                   AlertMessageBuilder messageBuilder,
                                                   AlertMessagesConstructor messagesConstructor) {
        return new OperationController(dataProcessingStack, operationProcessor, messageBuilder, messagesConstructor);
    }

    @Bean
    public ValidationController validationController(InvocationProcessor serviceProvider,
                                                     DomainProcessor domainProcessor) {
        return new ValidationController(serviceProvider, domainProcessor);
    }

    @Bean
    public DataController dataController(ControllerFactory controllerFactory,
                                         MetadataEnvironment environment,
                                         MetadataRouter router) {
        DataController dataController = new DataController(controllerFactory, environment, router);
        dataController.setMessageBuilder(messageBuilder(environment));
        return dataController;
    }

    @Bean
    public ExportController exportController(MetadataEnvironment environment, DataController dataController,
                                             FileGeneratorFactory fileGeneratorFactory) {
        return new ExportController(environment, dataController, fileGeneratorFactory);
    }

    @Bean
    public AlertMessageBuilder messageBuilder(MetadataEnvironment environment) {
        return new AlertMessageBuilder(environment.getMessageSource(), environment.getSystemProperties(), showStacktrace);
    }

    @Bean
    @ConditionalOnMissingBean
    public AlertMessagesConstructor messagesConstructor(AlertMessageBuilder messageBuilder) {
        return new N2oAlertMessagesConstructor(messageBuilder);
    }

}

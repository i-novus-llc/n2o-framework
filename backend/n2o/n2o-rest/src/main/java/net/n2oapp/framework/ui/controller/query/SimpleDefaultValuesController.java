package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.springframework.stereotype.Controller;

/**
 * Контроллер получения данных для значений по умолчанию
 */
@Controller
public class SimpleDefaultValuesController extends DefaultValuesController {

    public SimpleDefaultValuesController(DataProcessingStack dataProcessingStack,
                                         QueryProcessor queryProcessor,
                                         SubModelsProcessor subModelsProcessor,
                                         AlertMessageBuilder messageBuilder,
                                         MetadataEnvironment environment) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder, environment);
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.defaults;
    }
}

package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

/**
 * Контроллер получения данных для значений по умолчанию
 */
public class SimpleDefaultValuesController extends DefaultValuesController {

    public SimpleDefaultValuesController(DataProcessingStack dataProcessingStack,
                                         QueryProcessor queryProcessor,
                                         SubModelsProcessor subModelsProcessor,
                                         AlertMessageBuilder messageBuilder) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder);
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.defaults;
    }
}

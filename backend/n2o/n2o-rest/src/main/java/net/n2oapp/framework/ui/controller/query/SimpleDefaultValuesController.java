package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.framework.api.rest.ControllerType;
import org.springframework.stereotype.Controller;

/**
 * Контроллер получения данных для значений по умолчанию
 */
@Controller
public class SimpleDefaultValuesController extends DefaultValuesController {

    @Override
    public ControllerType getControllerType() {
        return ControllerType.defaults;
    }
}

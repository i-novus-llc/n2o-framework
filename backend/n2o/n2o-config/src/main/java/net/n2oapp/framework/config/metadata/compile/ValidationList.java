package net.n2oapp.framework.config.metadata.compile;

import lombok.Getter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Список скомпилированных валидаций, для передачи в scope CompileProcessor
 */
@Getter
public class ValidationList implements Compiled {
    private Map<ReduxModel, Map<String, List<Validation>>> validations;

    public ValidationList(Map<ReduxModel, Map<String, List<Validation>>> validations) {
        this.validations = validations;
    }

    public List<Validation> get(String widgetId, ReduxModel model) {
        return validations.containsKey(model) ? validations.get(model).get(widgetId) : Collections.emptyList();
    }
}

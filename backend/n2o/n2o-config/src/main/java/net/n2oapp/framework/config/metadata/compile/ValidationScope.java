package net.n2oapp.framework.config.metadata.compile;

import lombok.Getter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

import java.util.*;

/**
 * Карта скомпилированных валидаций, разложенная по модели и источнику данных
 */
@Getter
public class ValidationScope implements Compiled {
    private Map<ReduxModelEnum, Map<String, List<Validation>>> validations;

    public ValidationScope() {
        this.validations = new EnumMap<>(ReduxModelEnum.class);
    }


    public List<Validation> get(String datasourceId, ReduxModelEnum model) {
        if (validations.containsKey(model) && validations.get(model).containsKey(datasourceId))
            return validations.get(model).get(datasourceId);
        else
            return Collections.emptyList();
    }

    public void add(String datasourceId, ReduxModelEnum model, Validation validation) {
        validations.putIfAbsent(model, new HashMap<>());
        validations.get(model).putIfAbsent(datasourceId, new ArrayList<>());
        get(datasourceId, model).add(validation);
    }

    public void addAll(String datasourceId, ReduxModelEnum model, List<Validation> validations) {
        validations.forEach(v -> add(datasourceId, model, v));
    }
}

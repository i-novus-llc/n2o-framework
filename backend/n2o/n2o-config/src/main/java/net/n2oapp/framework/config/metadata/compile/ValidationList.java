package net.n2oapp.framework.config.metadata.compile;

import lombok.Getter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.util.*;

/**
 * Карта скомпилированных валидаций разложенная по модели и источнику данных
 */
@Getter
public class ValidationList implements Compiled {
    private Map<ReduxModel, Map<String, List<Validation>>> validations;

    public ValidationList() {
        this.validations = new EnumMap<>(ReduxModel.class);
    }


    public List<Validation> get(String datasourceId, ReduxModel model) {
        if (validations.containsKey(model) && validations.get(model).containsKey(datasourceId))
            return validations.get(model).get(datasourceId);
        else
            return Collections.emptyList();
    }

    public void add(String datasourceId, ReduxModel model, Validation validation) {
        validations.putIfAbsent(model, new HashMap<>());
        validations.get(model).putIfAbsent(datasourceId, new ArrayList<>());
        get(datasourceId, model).add(validation);
    }

    public void addAll(String datasourceId, ReduxModel model, List<Validation> validations) {
        validations.forEach(v -> add(datasourceId, model, v));
    }
}

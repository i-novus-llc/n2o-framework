package net.n2oapp.framework.api.metadata.local;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;

import java.util.*;

/**
 * Скомпилированный объект
 */
@Getter
@Setter
public class CompiledObject implements Compiled, IdAware {
    public static final String VALIDATION_RESULT_PARAM = "validation";

    private String id;
    private String name;
    private Map<String, Operation> operations;
    private List<AbstractParameter> objectFields;
    private Map<String, AbstractParameter> objectFieldsMap;
    //все валидации объекта
    private List<Validation> validations;
    //все валидации объекта разложенные в мапу по идентификаторам
    private Map<String, Validation> validationsMap;
    private String tableName;
    private String appName;
    private String moduleName;
    private String serviceName;

    private List<Validation> fieldValidations;

    public Map<String, AbstractParameter> getObjectFieldsMap() {
        return objectFieldsMap;
    }

    public List<AbstractParameter> getObjectFields() {
        return objectFields;
    }

    public void addValidation(Validation v) {
        if (validations == null) validations = new ArrayList<>();
        if (validationsMap == null) validationsMap = new HashMap<>();

        validationsMap.put(v.getId(), v);
        validations.add(v);
    }

    @Getter
    @Setter
    public static class Operation extends N2oObject.Operation implements Compiled, PropertiesAware {
        private Map<String, Object> properties;
        private Map<String, AbstractParameter> inParametersMap;
        private Map<String, ObjectSimpleField> outParametersMap;
        private Map<String, ObjectSimpleField> failOutParametersMap;
        private List<Validation> validationList;
        private Map<String, Validation> validationsMap;
        private Map<String, Validation> whiteListValidationsMap;

        public Map<String, Validation> getWhiteListValidationsMap() {
            return whiteListValidationsMap != null ? whiteListValidationsMap : Collections.emptyMap();
        }

        public void setWhiteListValidationsMap(Map<String, Validation> whiteListValidationsMap) {
            this.whiteListValidationsMap = whiteListValidationsMap;
        }

        public boolean isValidationEnable() {
            return getValidationList() != null && !getValidationList().isEmpty();
        }

        public Map<String, Validation> getValidationsMap() {
            return validationsMap != null ? validationsMap : Collections.emptyMap();
        }
    }
}

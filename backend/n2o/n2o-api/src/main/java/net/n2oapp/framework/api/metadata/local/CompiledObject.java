package net.n2oapp.framework.api.metadata.local;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.global.aware.OriginAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;

import java.util.*;

/**
 * Сокмпилированный объект
 */
@Getter
@Setter
public class CompiledObject implements CompiledMetadata {
    public static final String READ_ACTION = "read";
    public static final String VALIDATION_RESULT_PARAM = "validation";

    private String id;
    private String name;
    private Map<String, Operation> operations;
    private List<AbstractParameter> objectFields;
    private Map<String, AbstractParameter> objectFieldsMap;
    private Map<String, ObjectReferenceField> objectReferenceFieldsMap;
    //все валидации объекта
    private List<Validation> validations;
    //все валидации объекта разложенные в мапу по идентификаторам
    private Map<String, Validation> validationsMap;
    private String tableName;
    private String entityClass;
    private String appName;
    private String moduleName;
    private String serviceClass;
    private String serviceName;

    private List<Validation> fieldValidations;

    @Override
    public final Class<? extends CompiledMetadata> getCompiledBaseClass() {
        return CompiledObject.class;
    }

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
    public static class Operation extends N2oObject.Operation implements Compiled, OriginAware, PropertiesAware {

        private Map<String, Object> properties;
        private Map<String, N2oObject.Parameter> inParametersMap;
        private Map<String, N2oObject.Parameter> outParametersMap;
        private List<Validation> validationList;
        private List<ConditionValidation> conditionList;
        private Map<String, Validation> validationsMap;
        private Map<String, Validation> whiteListValidationsMap;
        private Set<String> inParamsSet = new LinkedHashSet<>();
        private Set<String> outParamsSet = new LinkedHashSet<>();
        private Boolean auto;

        public Operation(
                Map<String, N2oObject.Parameter> inParametersMap,
                Map<String, N2oObject.Parameter> outParametersMap) {
            this.inParametersMap = inParametersMap;
            initInParamsSet(inParametersMap, inParamsSet);
            this.outParametersMap = outParametersMap;
            initInParamsSet(outParametersMap, outParamsSet);
        }

        private void initInParamsSet(Map<String, N2oObject.Parameter> parameterMap, Set<String> parameterSet) {
            if (parameterMap != null)
                for (String param : parameterMap.keySet()) {
                    String[] split = param.split("\\.");
                    String tmp = "";
                    for (String s : split) {
                        if (tmp.length() != 0) tmp += '.';
                        tmp += s;
                        parameterSet.add(tmp);
                    }
                }
        }


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

        @Override
        public boolean isReal() {
            return this.getObjectId() != null;
        }

        public Boolean getAuto() {
            return auto;
        }

        public void setAuto(Boolean auto) {
            this.auto = auto;
        }
    }

    public Map<String, Operation> getOperations() {
        return operations;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Class<N2oObject> getSourceClass() {
        return N2oObject.class;
    }


}

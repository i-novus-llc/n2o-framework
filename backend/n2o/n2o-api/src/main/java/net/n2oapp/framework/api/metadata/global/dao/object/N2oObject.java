package net.n2oapp.framework.api.metadata.global.dao.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Исходная модель объекта
 */
@Getter
@Setter
public class N2oObject extends N2oMetadata implements NameAware {
    private String name;
    private String tableName;
    private String entityClass;
    private String appName;
    private String moduleName;
    private String serviceClass;
    private String serviceName;
    private Operation[] operations;
    private N2oValidation[] n2oValidations;
    private AbstractParameter[] objectFields;

    @Override
    public final String getPostfix() {
        return "object";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oObject.class;
    }

    @Getter
    @Setter
    public static class Operation implements Source, IdAware, ExtensionAttributesAware {
        private String id;
        private String name;
        private String formSubmitLabel;
        private String note;
        private String confirmationText;
        private Boolean confirm;
        private String bulkConfirmationText;
        private String successText;
        private String failText;
        private String description;

        private N2oInvocation invocation;
        private AbstractParameter[] inFields;
        private ObjectSimpleField[] outFields;
        private ObjectSimpleField[] failOutFields;

        private Validations validations;
        private Map<N2oNamespace, Map<String, String>> extAttributes;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return Objects.equals(id, ((Operation) o).id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }

        @Getter
        @Setter
        public static class Validations implements Serializable {
            private String[] whiteList;
            private String[] blackList;
            private Validation[] refValidations;
            private N2oValidation[] inlineValidations;

            @Getter
            @Setter
            public static class Validation implements Serializable {
                private String refId;
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Parameter extends InvocationParameter {
        private Parameter[] childParams;
        private String param;
        private String validationFailKey;

        public Parameter(String name, String mapping) {
            this.setMapping(mapping);
            this.setId(name);
        }

        public Parameter(Parameter parameter) {
            super(parameter);
            setChildParams(parameter.getChildParams());
            setPluralityType(parameter.getPluralityType());
            setParam(parameter.getParam());
        }
    }
}

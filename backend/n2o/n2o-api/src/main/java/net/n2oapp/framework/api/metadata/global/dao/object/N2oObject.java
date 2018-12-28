package net.n2oapp.framework.api.metadata.global.dao.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;

import java.io.Serializable;
import java.util.Map;

/**
 * User: iryabov
 * Date: 05.02.13
 * Time: 16:51
 */
@Getter
@Setter
public class N2oObject extends N2oMetadata implements NameAware {

    public static final String ERROR_OBJECT_ID = "error";


    private String name;
    private String parent;
    private Operation[] operations;
    private N2oValidation[] n2oValidations;
    private AbstractParameter[] objectFields;
    private String tableName;
    private String entityClass;
    private String appName;
    private String moduleName;
    private String serviceClass;
    private String serviceName;

    @Override
    public void setId(String id) {
        super.setId(id);
        //todo убрать после того, как в Action пропадет objectId
        if (operations != null)
            for (Operation operation : operations) {
                operation.setObjectId(id);
            }
    }

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
        //нужно конфигуратору. Подлежит избавлению.
        @Deprecated
        private String objectId;

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
        private Parameter[] inParameters;
        private Parameter[] outParameters;
        private Validations validations;
        private Map<N2oNamespace, Map<String, String>> extAttributes;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Operation operation = (Operation) o;

            return id != null ? id.equals(operation.id) : operation.id == null;
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }

        @Getter
        @Setter
        public static class Validations implements Serializable {
            private Activate activate;
            private String[] whiteList;
            private String[] blackList;
            private Validation[] refValidations;
            private N2oValidation[] inlineValidations;

            public enum Activate {
                nothing, all, whiteList, blackList
            }

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

        private Type type;
        private Parameter[] childParams;

        public Parameter(Type type, String name, String mapping) {
            this(type);
            this.setMapping(mapping);
            this.setId(name);
        }

        public Parameter(Type type) {
            this.type = type;
        }

        public Parameter(Parameter srcParam) {
            setType(srcParam.getType());
            setDomain(srcParam.getDomain());
            setMapping(srcParam.getMapping());
            setId(srcParam.getId());
            setDefaultValue(srcParam.getDefaultValue());
            setNormalize(srcParam.getNormalize());
            setMappingCondition(srcParam.getMappingCondition());
            setEntityClass(srcParam.getEntityClass());
            setChildParams(srcParam.getChildParams());
            setNullIgnore(srcParam.getNullIgnore());
            setPluralityType(srcParam.getPluralityType());
            setDefaultValue(srcParam.getDefaultValue());
        }

        public enum Type {
            in, out
        }
    }
}

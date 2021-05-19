package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Валидатор метаданных объекта
 */
@Component
public class N2oObjectValidator extends TypedMetadataValidator<N2oObject> {

    @Value("${n2o.config.java.mapping}")
    private String mapping;

    @Override
    public Class<N2oObject> getSourceClass() {
        return N2oObject.class;
    }

    @Override
    public void validate(N2oObject n2oObject, ValidateProcessor p) {
        if (mapping.equals("map"))
            if (n2oObject.getOperations() != null)
                for (N2oObject.Operation operation : n2oObject.getOperations())
                    if (operation.getInvocation() instanceof N2oJavaDataProvider)
                        checkJavaDataProvider(p, operation, n2oObject.getId());
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    private void checkJavaDataProvider(ValidateProcessor p, N2oObject.Operation operation, String objectId) {
        N2oJavaDataProvider n2oJavaDataProvider = ((N2oJavaDataProvider) operation.getInvocation());
        if (n2oJavaDataProvider.getArguments() != null) {
            for (Argument arg : n2oJavaDataProvider.getArguments()) {
                p.checkNotNull(arg.getName(),
                        String.format("В объекте %s для всех аргументов с java provider должно быть задано имя", objectId));
            }
        }
    }

}
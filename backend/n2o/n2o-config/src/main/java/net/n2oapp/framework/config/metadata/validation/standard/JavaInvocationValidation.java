package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

/**
 * Проверка на наличие class и entity-class при заданном type='class' и type='entity' соответственно в аргументах метода
 */
@Component
public class JavaInvocationValidation implements SourceValidator<N2oObject>, SourceClassAware {

    @Override
    public void validate(N2oObject n2oObject) throws N2oMetadataValidationException {
        if (n2oObject.getOperations() != null) {
            for (N2oObject.Operation operation : n2oObject.getOperations()) {
                N2oInvocation invocation = operation.getInvocation();
                if (invocation instanceof JavaInvocation && ((JavaInvocation) invocation).getArguments() != null) {
                    for (Argument argument : ((JavaInvocation) invocation).getArguments()) {
                        checkClassName(argument);
                        checkEntityClass(argument, n2oObject);
                    }

                }

            }

        }

    }

    /**
     * Проверяет наличие class при заданном type='class'
     */
    private static void checkClassName(Argument argument) {
        if (argument.getType().equals(Argument.Type.CLASS) && argument.getClassName() == null) {
            throw new N2oMetadataValidationException("For argument " + argument.getName() + " set type='class', but class doesn't set");
        }

    }

    /**
     * Проверка наличие entity-class при заданном type='entity'
     */
    private static void checkEntityClass(Argument argument, N2oObject n2oObject) {
        if (argument.getType().equals(Argument.Type.ENTITY) && n2oObject.getEntityClass() == null) {
            throw new N2oMetadataValidationException("For argument " + argument.getName() + " set type='entity', but entity-class doesn't set");
        }

    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oObject.class;
    }

}

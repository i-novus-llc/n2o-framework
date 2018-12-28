package net.n2oapp.framework.config.metadata.transformer;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.EjbInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.context.*;
import net.n2oapp.framework.api.transformer.SourceTransformer;

/**
 * Копирует параметры application и module из объекта в действия и валидации ejbInvocation
 */
public class EjbInvocationTransformer implements SourceTransformer<N2oObject, CompileContext> {

    @Override
    public N2oObject transformBeforeCompile(N2oObject n2oObject, CompileContext context) {
        if (n2oObject.getOperations() != null) {
            for (N2oObject.Operation operation : n2oObject.getOperations()) {
                if (operation.getInvocation() != null && operation.getInvocation() instanceof EjbInvocation) {
                    EjbInvocation ejbInvocation = (EjbInvocation) operation.getInvocation();
                    if (ejbInvocation.getApplication() == null) {
                        ejbInvocation.setApplication(n2oObject.getAppName());
                    }
                    if (ejbInvocation.getModule() == null) {
                        ejbInvocation.setModule(n2oObject.getModuleName());
                    }
                }
            }
        }

        if (n2oObject.getN2oValidations() != null) {
            for (N2oValidation validation : n2oObject.getN2oValidations()) {
                if (validation instanceof N2oConstraint) {
                    N2oConstraint n2oConstraint = (N2oConstraint) validation;
                    if (n2oConstraint.getN2oInvocation() instanceof EjbInvocation) {
                        EjbInvocation ejbInvocation = (EjbInvocation) n2oConstraint.getN2oInvocation();
                        if (ejbInvocation.getApplication() == null) {
                            ejbInvocation.setApplication(n2oObject.getAppName());
                        }
                        if (ejbInvocation.getModule() == null) {
                            ejbInvocation.setModule(n2oObject.getModuleName());
                        }
                    }
                }
            }
        }
        return n2oObject;
    }

    @Override
    public Class<N2oObject> getMetadataClass() {
        return N2oObject.class;
    }

    @Override
    public Class<CompileContext> getContextClass() {
        return CompileContext.class;
    }
}

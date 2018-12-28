package net.n2oapp.framework.config.metadata.transformer;

import net.n2oapp.framework.api.metadata.domain.Domain;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectScalarField;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.transformer.SourceTransformer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Копирует недостающие параметры из объекта в invocation
 * и проставляет тип аргумента по дефолту
 */
@Component
public class JavaInvocationTransformer implements SourceTransformer<N2oObject, CompileContext> {
    private Map<String, String> primitivesByDomain = findPrimitiveByDomain();

    @Override
    public N2oObject transformBeforeCompile(N2oObject n2oObject, CompileContext context) {
        if (n2oObject.getOperations() != null) {
            for (N2oObject.Operation operation : n2oObject.getOperations()) {
                if (operation.getInvocation() != null && operation.getInvocation() instanceof JavaInvocation) {
                    JavaInvocation javaInvocation = (JavaInvocation) operation.getInvocation();

                    defineMethodClassByObject(n2oObject, javaInvocation);
                    if (javaInvocation.getArguments() != null) {
                        for (int i = 0; i < javaInvocation.getArguments().length; i++) {
                            Argument argument = javaInvocation.getArguments()[i];
                            defineArgumentClassByEntity(n2oObject, argument);
                            setDefaultArgumentType(argument);
                            definePrimitiveClass(n2oObject, operation, argument, i);
                        }
                    }
                }
            }
        }
        return n2oObject;
    }

    /**
     * Проставляет класс вызываемого метода по service-class из объекта
     *
     * @param n2oObject
     * @param javaInvocation параметры метода
     */
    private void defineMethodClassByObject(N2oObject n2oObject, JavaInvocation javaInvocation) {
        if ((javaInvocation.getClassName() == null) && (n2oObject.getServiceClass() != null)) {
            javaInvocation.setClassName(n2oObject.getServiceClass());
        }
    }

    /**
     * Проставляет класс аргумента по entity-class
     *
     * @param n2oObject
     * @param argument  аргумент метода
     */
    private void defineArgumentClassByEntity(N2oObject n2oObject, Argument argument) {
        if (argument.getType() != null && argument.getType().equals(Argument.Type.ENTITY)) {
            if (argument.getClassName() == null) {
                argument.setClassName(n2oObject.getEntityClass());
            }
        }
    }

    /**
     * Проставляет тип аргумента по дефолту
     *
     * @param argument аргумент метода
     */
    private void setDefaultArgumentType(Argument argument) {
        if (argument.getType() == null) {
            if (argument.getClassName() == null) {
                argument.setType(Argument.Type.PRIMITIVE);
            } else {
                argument.setType((Argument.Type.CLASS));
            }
        }
    }

    /**
     * Определяет класс аргумента
     *
     * @param n2oObject
     * @param argument  аргумент метода
     */
    private void definePrimitiveClass(N2oObject n2oObject, N2oObject.Operation operation, Argument argument, int argumentPosition) {
        if ((argument.getType() != null)
                && (argument.getType().equals(Argument.Type.PRIMITIVE))
                && argument.getClassName() == null) {
            primitiveByInParameters(operation, argument, argumentPosition);
            if (argument.getClassName() != null) {
                return;
            }
            primitiveByFields(n2oObject, argument);
            if (argument.getClassName() == null) {
                argument.setClassName("java.lang.String");
            }
        }
    }

    /**
     * Определяет класс аргумента по innMapping
     *
     * @param argument аргумент метода
     */
    private void primitiveByInParameters(N2oObject.Operation operation, Argument argument, int argumentPosition) {
        if ((operation.getInParameters() != null)) {
            for (N2oObject.Parameter innParameter : operation.getInParameters()) {
                if (innParameter.getMapping().startsWith("[")) {
                    int innParameterPosition = Integer.parseInt(innParameter.getMapping().replaceAll("[\\[\\]]", ""));
                    if ((argumentPosition == innParameterPosition)
                            && (innParameter.getDomain() != null)) {
                        argument.setClassName(primitivesByDomain.get(innParameter.getDomain()));
                    }
                }
            }
        }
    }

    /**
     * Определяет класс аргумента по полям
     *
     * @param n2oObject
     * @param argument  аргумент метода
     */
    private void primitiveByFields(N2oObject n2oObject, Argument argument) {
        if (n2oObject.getObjectFields() != null) {
            for (AbstractParameter field : n2oObject.getObjectFields()) {
                if (field instanceof ObjectScalarField) {
                    ObjectScalarField objectScalarField = (ObjectScalarField) field;
                    if ((field.getId().equals(argument.getName()))
                            && (objectScalarField.getDomain() != null)) {
                        argument.setClassName(primitivesByDomain.get(objectScalarField.getDomain()));
                    }
                }
            }
        }
    }

    /**
     * Заполняет map соответствиями между именем domain и наименованием класса
     */
    private Map<String, String> findPrimitiveByDomain() {
        Map<String, String> primitivesByDomain = new HashMap<>();
        for (Domain domain : Domain.values()) {
            primitivesByDomain.put(domain.getName(), domain.getTypeClass().getName());
        }
        return primitivesByDomain;
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

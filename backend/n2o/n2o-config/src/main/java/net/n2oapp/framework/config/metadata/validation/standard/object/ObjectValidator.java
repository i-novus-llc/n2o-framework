package net.n2oapp.framework.config.metadata.validation.standard.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oInvocationValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oMandatory;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.InvocationScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидатор объекта
 */
@Component
public class ObjectValidator implements SourceValidator<N2oObject>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oObject.class;
    }

    @Override
    public void validate(N2oObject object, SourceProcessor p) {
        InvocationScope invocationScope = new InvocationScope();
        invocationScope.setObjectId(object.getId());
        if (!ArrayUtils.isEmpty(object.getObjectFields())) {
            p.safeStreamOf(object.getObjectFields()).forEach(field ->
                    checkFieldIdExistence(field, p, String.format("В одном из полей объекта %s не указан 'id'",
                            ValidationUtils.getIdOrEmptyString(object.getId()))));
            checkForExistsReferenceObject(object.getId(), object.getObjectFields(), p);
        }

        p.checkIdsUnique(object.getObjectFields(), String.format("Поле '%s' встречается более чем один раз в объекте %s", "%s",
                ValidationUtils.getIdOrEmptyString(object.getId())));
        p.checkIdsUnique(object.getOperations(), String.format("Действие '%s' встречается более чем один раз в объекте %s", "%s",
                ValidationUtils.getIdOrEmptyString(object.getId())));
        p.checkIdsUnique(object.getN2oValidations(), String.format("Валидация '%s' встречается более чем один раз в объекте %s", "%s",
                ValidationUtils.getIdOrEmptyString(object.getId())));

        if (object.getOperations() != null)
            validateOperations(object, p, invocationScope);

        if (object.getN2oValidations() != null)
            p.safeStreamOf(object.getN2oValidations()).forEach(validation -> {
                p.checkIdExistence(validation,
                        String.format("В одной из валидаций объекта %s не указан параметр 'id'",
                                ValidationUtils.getIdOrEmptyString(object.getId())));
                if (validation instanceof N2oMandatory) {
                    if (validation.getFieldId() == null)
                        throw new N2oMetadataValidationException(String.format("В <mandatory> валидации %s объекта %s необходимо указать атрибут 'field-id'",
                                ValidationUtils.getIdOrEmptyString(validation.getId()),
                                ValidationUtils.getIdOrEmptyString(object.getId())));
                }
                if (validation instanceof N2oInvocationValidation) {
                    invocationScope.setValidationId(validation.getId());
                    p.validate(((N2oInvocationValidation) validation).getN2oInvocation(), invocationScope);
                }
            });
    }

    /**
     * Проверка атрибута side в валидации операции
     *
     * @param object Исходная metadata объекта
     * @param p        Процессор исходных метаданных
     */
    private void validateOperations(N2oObject object, SourceProcessor p, InvocationScope invocationScope) {
        p.safeStreamOf(object.getOperations()).forEach(operation -> {
            if (operation.getId() == null)
                throw new N2oMetadataValidationException(String.format("В одной из операций объекта %s не указан 'id'",
                        ValidationUtils.getIdOrEmptyString(object.getId())));
            invocationScope.setOperationId(operation.getId());
            if (operation.getInFields() != null)
                p.safeStreamOf(operation.getInFields()).forEach(field ->
                        checkFieldIdExistence(field, p, String.format("В одном из <in> полей операции %s объекта %s не указан 'id'",
                                ValidationUtils.getIdOrEmptyString(operation.getId()),
                                ValidationUtils.getIdOrEmptyString(object.getId()))));

            if (operation.getOutFields() != null)
                p.safeStreamOf(operation.getOutFields()).forEach(field ->
                        checkFieldIdExistence(field, p, String.format("В одном из <out> полей операции %s объекта %s не указан 'id'",
                                ValidationUtils.getIdOrEmptyString(operation.getId()),
                                ValidationUtils.getIdOrEmptyString(object.getId()))));

            p.checkIdsUnique(operation.getInFields(), String.format("Поле '%s' встречается более чем один раз в <in> операции %s объекта %s", "%s",
                    ValidationUtils.getIdOrEmptyString(operation.getId()),
                    ValidationUtils.getIdOrEmptyString(object.getId())));
            p.checkIdsUnique(operation.getOutFields(), String.format("Поле '%s' встречается более чем один раз в <out> операции %s объекта %s", "%s",
                    ValidationUtils.getIdOrEmptyString(operation.getId()),
                    ValidationUtils.getIdOrEmptyString(object.getId())));

            if (operation.getInFields() != null)
                checkForExistsReferenceObject(object.getId(), operation.getInFields(), p);

            if (operation.getInvocation() != null) {
                p.validate(operation.getInvocation(), invocationScope);
            }

            if (operation.getValidations() != null) {
                if (operation.getValidations().getWhiteList() != null)
                    checkValidationWhiteList(object, operation, p);
                if (operation.getValidations().getInlineValidations() != null)
                    p.safeStreamOf(operation.getValidations().getInlineValidations()).forEach(validation -> {
                        p.checkIdExistence(validation, String.format("В одной из валидаций операции %s объекта %s не указан параметр 'id'",
                                    ValidationUtils.getIdOrEmptyString(operation.getId()),
                                    ValidationUtils.getIdOrEmptyString(object.getId())));
                        if (validation instanceof N2oInvocationValidation) {
                            invocationScope.setValidationId(validation.getId());
                            p.validate(((N2oInvocationValidation) validation).getN2oInvocation(), invocationScope);
                        }
                    });
            }
            checkValidationSide(object.getId(), operation);
        });
    }

    /**
     * Проверка атрибута side в валидации операции
     *
     * @param objectId  Идентификатор текущего объекта
     * @param operation Операция текущего объекта
     */
    private void checkValidationSide(String objectId, N2oObject.Operation operation) {
        if (operation.getValidations() != null)
            for (N2oValidation validation : operation.getValidations().getInlineValidations()) {
                if (validation.getSide() != null)
                    if (validation.getSide().contains("client"))
                        throw new N2oMetadataValidationException(
                                String.format("Атрибут 'side' валидации %s операции %s объекта %s не может иметь значение client",
                                        getIdOrEmptyString(validation.getId()),
                                        getIdOrEmptyString(operation.getId()),
                                        getIdOrEmptyString(objectId)));
            }
    }

    /**
     * Проверка существования объектов, на которые могут ссылаться поля текущего объекта
     *
     * @param objectId Идентификатор текущего объекта
     * @param fields   Список полей, проверяемых на наличие ссылки
     * @param p        Процессор исходных метаданных
     */
    private void checkForExistsReferenceObject(String objectId, AbstractParameter[] fields, SourceProcessor p) {
        for (AbstractParameter field : fields) {
            if (field instanceof ObjectReferenceField) {
                ObjectReferenceField refField = (ObjectReferenceField) field;
                if (refField.getReferenceObjectId() != null)
                    p.checkForExists(refField.getReferenceObjectId(), N2oObject.class,
                            String.format("Поле %s в объекте %s ссылается на несуществующий объект %s",
                                    ValidationUtils.getIdOrEmptyString(refField.getId()),
                                    ValidationUtils.getIdOrEmptyString(objectId),
                                    ValidationUtils.getIdOrEmptyString(refField.getReferenceObjectId())));
                if (refField.getFields() != null && refField.getFields().length != 0)
                    checkForExistsReferenceObject(objectId, refField.getFields(), p);
            }
        }
    }

    /**
     * Проверка атрибута white-list в валидации операции
     *
     * @param object    Исходная metadata объекта
     * @param operation Исходная metadata операции
     * @param p         Процессор исходных метаданных
     */
    private void checkValidationWhiteList(N2oObject object, N2oObject.Operation operation, SourceProcessor p) {
        if (object.getN2oValidations() == null)
            throw new N2oMetadataValidationException(String.format("В валидации операции %s объекта %s указан атрибут 'white-list', но сам объект не имеет валидаций",
                    ValidationUtils.getIdOrEmptyString(operation.getId()),
                    ValidationUtils.getIdOrEmptyString(object.getId())));
        List<String> validationsIds = p.safeStreamOf(object.getN2oValidations()).map(N2oValidation::getId).collect(Collectors.toList());
        p.safeStreamOf(operation.getValidations().getWhiteList()).forEach(val -> {
            if (!validationsIds.contains(val))
                throw new N2oMetadataValidationException(String.format("Атрибут 'white-list' операции %s объекта %s ссылается на несуществующую валидацию %s объекта",
                        ValidationUtils.getIdOrEmptyString(operation.getId()),
                        ValidationUtils.getIdOrEmptyString(object.getId()),
                        ValidationUtils.getIdOrEmptyString(val)));
        });
    }

    /**
     * Проверка полей на наличие id
     *
     * @param field   Исходная metadata поля
     * @param p       Процессор исходных метаданных
     * @param message Сообщение об ошибке
     */
    public static void checkFieldIdExistence(AbstractParameter field, SourceProcessor p, String message) {
        if (field instanceof ObjectReferenceField)
            p.safeStreamOf(((ObjectReferenceField) field).getFields()).forEach(childField -> checkFieldIdExistence(childField, p, message));
        p.checkIdExistence(field, message);
    }
}

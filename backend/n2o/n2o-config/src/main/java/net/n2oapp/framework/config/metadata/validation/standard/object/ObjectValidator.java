package net.n2oapp.framework.config.metadata.validation.standard.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oInvocationValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oMandatoryValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.InvocationScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkSimpleFieldSwitchCase;
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
                            getIdOrEmptyString(object.getId()))));
            checkForExistsReferenceObject(object.getId(), object.getObjectFields(), p);
        }

        p.checkIdsUnique(object.getObjectFields(),
                String.format("Поле '%s' встречается более чем один раз в объекте %s", "%s",
                        getIdOrEmptyString(object.getId())));
        p.checkIdsUnique(object.getOperations(),
                String.format("Операция '%s' встречается более чем один раз в объекте %s", "%s",
                        getIdOrEmptyString(object.getId())));
        p.checkIdsUnique(object.getN2oValidations(),
                String.format("Валидация '%s' встречается более чем один раз в объекте %s", "%s",
                        getIdOrEmptyString(object.getId())));

        p.safeStreamOf(object.getOperations())
                .forEach(operation -> validateOperation(object, operation, p, invocationScope));

        if (object.getN2oValidations() != null)
            p.safeStreamOf(object.getN2oValidations()).forEach(validation -> {
                p.checkIdExistence(validation,
                        String.format("В одной из валидаций объекта %s не указан параметр 'id'",
                                getIdOrEmptyString(object.getId())));
                if (validation instanceof N2oMandatoryValidation && validation.getFieldId() == null)
                    throw new N2oMetadataValidationException(
                            String.format("В \"<mandatory>\" валидации %s объекта %s необходимо указать атрибут 'field-id'",
                                    getIdOrEmptyString(validation.getId()),
                                    getIdOrEmptyString(object.getId())));
                if (validation instanceof N2oInvocationValidation invocationValidation) {
                    invocationScope.setValidationId(validation.getId());
                    p.validate(invocationValidation.getN2oInvocation(), invocationScope);
                }
            });
    }

    /**
     * Валидация операции объекта
     *
     * @param object          Исходная metadata объекта
     * @param operation       Операция текущего объекта
     * @param p               Процессор исходных метаданных
     * @param invocationScope Скоуп валидации провайдеров данных
     */
    private void validateOperation(N2oObject object, N2oObject.Operation operation, SourceProcessor p, InvocationScope invocationScope) {
        if (operation.getId() == null)
            throw new N2oMetadataValidationException(
                    String.format("В одной из операций объекта %s не указан 'id'",
                            getIdOrEmptyString(object.getId())));

        invocationScope.setOperationId(operation.getId());
        validateOperationInOutFields(object, operation, p);

        if (operation.getInvocation() != null)
            p.validate(operation.getInvocation(), invocationScope);

        validateOperationValidations(object, operation, p, invocationScope);
    }

    private void validateOperationInOutFields(N2oObject object, N2oObject.Operation operation, SourceProcessor p) {
        if (operation.getInFields() != null) {
            p.safeStreamOf(operation.getInFields()).forEach(field ->
                    checkFieldIdExistence(field, p,
                            String.format("В одном из <in> полей операции %s объекта %s не указан 'id'",
                                    getIdOrEmptyString(operation.getId()),
                                    getIdOrEmptyString(object.getId()))));
            checkForExistsReferenceObject(object.getId(), operation.getInFields(), p);
            checkSwitchCase(operation.getInFields());
        }
        if (operation.getOutFields() != null) {
            p.safeStreamOf(operation.getOutFields()).forEach(field ->
                    checkFieldIdExistence(field, p,
                            String.format("В одном из <out> полей операции %s объекта %s не указан 'id'",
                                    getIdOrEmptyString(operation.getId()),
                                    getIdOrEmptyString(object.getId()))));
            checkSwitchCase(operation.getOutFields());
        }
        p.checkIdsUnique(operation.getInFields(),
                String.format("Поле '%s' встречается более чем один раз в <in> операции %s объекта %s", "%s",
                        getIdOrEmptyString(operation.getId()),
                        getIdOrEmptyString(object.getId())));
        p.checkIdsUnique(operation.getOutFields(),
                String.format("Поле '%s' встречается более чем один раз в <out> операции %s объекта %s", "%s",
                        getIdOrEmptyString(operation.getId()),
                        getIdOrEmptyString(object.getId())));
    }

    private void validateOperationValidations(N2oObject object, N2oObject.Operation operation,
                                              SourceProcessor p, InvocationScope invocationScope) {
        if (operation.getValidations() == null)
            return;
        if (operation.getValidations().getWhiteList() != null)
            checkValidationWhiteList(object, operation, p);
        p.safeStreamOf(operation.getValidations().getInlineValidations())
                .forEach(validation -> {
                    p.checkIdExistence(validation, String.format("В одной из валидаций операции %s объекта %s не указан параметр 'id'",
                            getIdOrEmptyString(operation.getId()),
                            getIdOrEmptyString(object.getId())));
                    if (validation instanceof N2oInvocationValidation invocationValidation) {
                        invocationScope.setValidationId(validation.getId());
                        p.validate(invocationValidation.getN2oInvocation(), invocationScope);
                    }
                });
        p.safeStreamOf(operation.getValidations().getInlineValidations())
                .forEach(validation -> validationSideIsNotClient(object.getId(), operation, validation));

    }

    private void checkSwitchCase(AbstractParameter[] fields) {
        if (fields == null)
            return;
        for (AbstractParameter field : fields) {
            if (field instanceof ObjectReferenceField refField) {
                checkSwitchCase(refField.getFields());
            } else if (field instanceof ObjectSimpleField simpleField && simpleField.getN2oSwitch() != null) {
                checkSimpleFieldSwitchCase(simpleField.getN2oSwitch().getCases(), field.getId());
            }
        }
    }

    /**
     * Проверка атрибута side в валидации операции
     *
     * @param objectId  Идентификатор текущего объекта
     * @param operation Операция текущего объекта
     */
    private void validationSideIsNotClient(String objectId, N2oObject.Operation operation, N2oValidation validation) {
        if (validation.getSide() != null && validation.getSide().contains("client"))
            throw new N2oMetadataValidationException(
                    String.format("Атрибут 'side' валидации %s операции %s объекта %s не может иметь значение 'client'",
                            getIdOrEmptyString(validation.getId()),
                            getIdOrEmptyString(operation.getId()),
                            getIdOrEmptyString(objectId)));
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
            if (field instanceof ObjectReferenceField refField) {
                if (refField.getReferenceObjectId() != null)
                    ValidationUtils.checkForExistsObject(refField.getReferenceObjectId(),
                            String.format("Поле %s в объекте %s",
                                    getIdOrEmptyString(refField.getId()),
                                    getIdOrEmptyString(objectId)), p);
                if (!ArrayUtils.isEmpty(refField.getFields()))
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
            throw new N2oMetadataValidationException(
                    String.format("В валидации операции %s объекта %s указан атрибут 'white-list', но сам объект не имеет валидаций",
                            getIdOrEmptyString(operation.getId()),
                            getIdOrEmptyString(object.getId())));

        Map<String, N2oValidation> validations = new HashMap<>();
        p.safeStreamOf(object.getN2oValidations()).forEach(v -> validations.put(v.getId(), v));
        p.safeStreamOf(operation.getValidations().getWhiteList()).forEach(val -> {
            if (!validations.containsKey(val))
                throw new N2oMetadataValidationException(
                        String.format("Атрибут 'white-list' операции %s объекта %s ссылается на несуществующую валидацию %s объекта",
                                getIdOrEmptyString(operation.getId()),
                                getIdOrEmptyString(object.getId()),
                                getIdOrEmptyString(val)));
            validationSideIsNotClient(object.getId(), operation, validations.get(val));
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
        if (field instanceof ObjectReferenceField refField)
            p.safeStreamOf(refField.getFields())
                    .forEach(childField -> checkFieldIdExistence(childField, p, message));
        p.checkIdExistence(field, message);
    }
}

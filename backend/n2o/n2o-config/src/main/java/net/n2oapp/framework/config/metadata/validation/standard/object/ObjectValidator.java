package net.n2oapp.framework.config.metadata.validation.standard.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oDialogValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oInvocationValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oMandatoryValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.InvocationScope;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
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

        if (nonNull(object.getOperations())) {
            validateOperations(object, p, invocationScope);

        }

        if (nonNull(object.getN2oValidations()))
            p.safeStreamOf(object.getN2oValidations()).forEach(validation -> {
                p.checkIdExistence(validation,
                        String.format("В одной из валидаций объекта %s не указан параметр 'id'",
                                getIdOrEmptyString(object.getId())));
                if (validation instanceof N2oMandatoryValidation && isNull(validation.getFieldId()))
                    throw new N2oMetadataValidationException(
                            String.format("В <mandatory> валидации %s объекта %s необходимо указать атрибут 'field-id'",
                                    getIdOrEmptyString(validation.getId()),
                                    getIdOrEmptyString(object.getId())));
                if (validation instanceof N2oInvocationValidation) {
                    invocationScope.setValidationId(validation.getId());
                    p.validate(((N2oInvocationValidation) validation).getN2oInvocation(), invocationScope);
                    if (validation instanceof N2oDialogValidation)
                        validateDialog(validation, String.format(" объекта %s",
                                getIdOrEmptyString(object.getId())));
                }
            });
    }

    private static void validateDialog(N2oValidation validation, String message) {
        if (((N2oDialogValidation) validation).getToolbar() != null
            && ((N2oDialogValidation) validation).getToolbar().getGenerate() == null
            && ((N2oDialogValidation) validation).getToolbar().getItems() == null)
        throw new N2oMetadataValidationException("Не заданы элементы или атрибут 'generate' в тулбаре валидации <dialog>" + message);
    }

    /**
     * Проверка атрибута side в валидации операции
     *
     * @param object Исходная metadata объекта
     * @param p      Процессор исходных метаданных
     */
    private void validateOperations(N2oObject object, SourceProcessor p, InvocationScope invocationScope) {
        p.safeStreamOf(object.getOperations()).forEach(operation -> {
            if (isNull(operation.getId()))
                throw new N2oMetadataValidationException(
                        String.format("В одной из операций объекта %s не указан 'id'",
                                getIdOrEmptyString(object.getId())));

            invocationScope.setOperationId(operation.getId());
            if (nonNull(operation.getInFields())) {
                p.safeStreamOf(operation.getInFields()).forEach(field ->
                        checkFieldIdExistence(field, p,
                                String.format("В одном из <in> полей операции %s объекта %s не указан 'id'",
                                        getIdOrEmptyString(operation.getId()),
                                        getIdOrEmptyString(object.getId()))));

                checkForExistsReferenceObject(object.getId(), operation.getInFields(), p);
                checkSwitchCase(operation.getInFields());
            }

            if (nonNull(operation.getOutFields())) {
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

            if (nonNull(operation.getInvocation()))
                p.validate(operation.getInvocation(), invocationScope);

            if (nonNull(operation.getValidations())) {
                if (nonNull(operation.getValidations().getWhiteList()))
                    checkValidationWhiteList(object, operation, p);
                if (nonNull(operation.getValidations().getInlineValidations()))
                    p.safeStreamOf(operation.getValidations().getInlineValidations()).forEach(validation -> {
                        p.checkIdExistence(validation, String.format("В одной из валидаций операции %s объекта %s не указан параметр 'id'",
                                getIdOrEmptyString(operation.getId()),
                                getIdOrEmptyString(object.getId())));
                        if (validation instanceof N2oInvocationValidation) {
                            invocationScope.setValidationId(validation.getId());
                            p.validate(((N2oInvocationValidation) validation).getN2oInvocation(), invocationScope);
                            if (validation instanceof N2oDialogValidation)
                                validateDialog(validation, String.format(" в операции %s объекта %s",
                                        getIdOrEmptyString(operation.getId()),
                                        getIdOrEmptyString(object.getId())));
                        }
                    });
            }
            checkValidationSide(object.getId(), operation);
        });
    }

    private void checkSwitchCase(AbstractParameter[] fields) {
        if (isNull(fields))
            return;
        for (AbstractParameter field : fields) {
            if (field instanceof ObjectReferenceField)
                checkSwitchCase(((ObjectReferenceField) field).getFields());
            if (field instanceof ObjectSimpleField && nonNull(((ObjectSimpleField) field).getN2oSwitch())) {
                N2oSwitch n2oSwitch = ((ObjectSimpleField) field).getN2oSwitch();
                if (n2oSwitch.getCases().isEmpty())
                    throw new N2oMetadataValidationException(
                            String.format("В элементе '<switch>' поля '%s' отсутствует '<case>'", field.getId()));
                if (n2oSwitch.getCases().containsKey(""))
                    throw new N2oMetadataValidationException(
                            String.format("В '<case>' элемента '<switch>' поля '%s' атрибут 'value' пустой", field.getId()));
                if (n2oSwitch.getCases().containsValue(""))
                    throw new N2oMetadataValidationException(
                            String.format("В '<case>' элемента '<switch>' поля '%s' отсутствует тело", field.getId()));
            }
        }
    }

    /**
     * Проверка атрибута side в валидации операции
     *
     * @param objectId  Идентификатор текущего объекта
     * @param operation Операция текущего объекта
     */
    private void checkValidationSide(String objectId, N2oObject.Operation operation) {
        if (nonNull(operation.getValidations()))
            for (N2oValidation validation : operation.getValidations().getInlineValidations()) {
                if (nonNull(validation.getSide()) && validation.getSide().contains("client"))
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
                if (nonNull(refField.getReferenceObjectId()))
                    p.checkForExists(refField.getReferenceObjectId(), N2oObject.class,
                            String.format("Поле %s в объекте %s ссылается на несуществующий объект %s",
                                    getIdOrEmptyString(refField.getId()),
                                    getIdOrEmptyString(objectId),
                                    getIdOrEmptyString(refField.getReferenceObjectId())));
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
        if (isNull(object.getN2oValidations()))
            throw new N2oMetadataValidationException(
                    String.format("В валидации операции %s объекта %s указан атрибут 'white-list', но сам объект не имеет валидаций",
                            getIdOrEmptyString(operation.getId()),
                            getIdOrEmptyString(object.getId())));

        List<String> validationsIds = p.safeStreamOf(object.getN2oValidations()).map(N2oValidation::getId).collect(Collectors.toList());
        p.safeStreamOf(operation.getValidations().getWhiteList()).forEach(val -> {
            if (!validationsIds.contains(val))
                throw new N2oMetadataValidationException(
                        String.format("Атрибут 'white-list' операции %s объекта %s ссылается на несуществующую валидацию %s объекта",
                                getIdOrEmptyString(operation.getId()),
                                getIdOrEmptyString(object.getId()),
                                getIdOrEmptyString(val)));
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
            p.safeStreamOf(((ObjectReferenceField) field).getFields())
                    .forEach(childField -> checkFieldIdExistence(childField, p, message));
        p.checkIdExistence(field, message);
    }
}

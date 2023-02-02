package net.n2oapp.framework.config.metadata.validation.standard.object;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oInvocationValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdInQuotesOrEmptyString;

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
        p.checkIdsUnique(object.getObjectFields(), "Поле {0} встречается более чем один раз в объекте " + object.getId());
        p.checkIdsUnique(object.getOperations(), "Действие {0} встречается более чем один раз в объекте " + object.getId());
        p.checkIdsUnique(object.getN2oValidations(), "Валидация {0} встречается более чем один раз в объекте " + object.getId());

        if (object.getObjectFields() != null && object.getObjectFields().length != 0)
            checkForExistsReferenceObject(object.getId(), object.getObjectFields(), p);

        if (object.getOperations() != null)
            for (N2oObject.Operation operation : object.getOperations()) {
                if (operation.getInFields() != null)
                    checkForExistsReferenceObject(object.getId(), operation.getInFields(), p);
                if (operation.getInvocation() != null)
                    p.validate(operation.getInvocation());
                checkValidationSide(object.getId(), operation);
            }
    }

    /**
     * Проверка атрибута side в валидации операции
     *
     * @param objectId Идентификатор текущего объекта
     * @param operation  Операция текущего объекта
     */
    private void checkValidationSide(String objectId, N2oObject.Operation operation) {
        if (operation.getValidations() != null)
            for (N2oValidation validation : operation.getValidations().getInlineValidations()) {
                if (validation.getSide() != null)
                    if (validation.getSide().contains("client"))
                        throw new N2oMetadataValidationException(
                                String.format("Атрибут 'side' валидации %s операции %s объекта %s не может иметь значение client",
                                        getIdInQuotesOrEmptyString(validation.getId()),
                                        getIdInQuotesOrEmptyString(operation.getId()),
                                        getIdInQuotesOrEmptyString(objectId)));
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
                            String.format("Поле '%s' в объекте '%s' ссылается на несуществующий объект '%s'",
                                    refField.getId(), objectId, refField.getReferenceObjectId()));
                if (refField.getFields() != null && refField.getFields().length != 0)
                    checkForExistsReferenceObject(objectId, refField.getFields(), p);
            }
        }
    }
}

package net.n2oapp.framework.config.io.object;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidationCondition;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;


/**
 * Чтение\запись объекта версии 2.0
 */
@Component
public class ObjectElementIOv2 implements NamespaceIO<N2oObject> {

    @Override
    public void io(Element e, N2oObject t, IOProcessor p) {
        p.element(e, "name", t::getName, t::setName);
        p.element(e, "table-name", t::getTableName, t::setTableName);
        p.element(e, "entity-class", t::getEntityClass, t::setEntityClass);
        p.element(e, "app-name", t::getAppName, t::setAppName);
        p.element(e, "module-name", t::getModuleName, t::setModuleName);
        p.element(e, "service-class", t::getServiceClass, t::setServiceClass);
        p.element(e, "service-name", t::getServiceName, t::setServiceName);

        p.anyChildren(e, "fields", t::getObjectFields, t::setObjectFields, p.oneOf(AbstractParameter.class)
                .add("field", ObjectSimpleField.class, this::field)
                .add("reference-field", ObjectReferenceField.class, this::referenceField));
        p.children(e, "actions", "action", t::getOperations, t::setOperations, N2oObject.Operation::new, this::action);
        p.anyChildren(e, "validations", t::getN2oValidations, t::setN2oValidations, p.oneOf(N2oValidation.class)
                .add("constraint", N2oConstraint.class, this::constraint)
                .add("condition", N2oValidationCondition.class, this::condition));
    }

    private void action(Element e, N2oObject.Operation t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "form-submit-label", t::getFormSubmitLabel, t::setFormSubmitLabel);
        p.anyChild(e, "invocation", t::getInvocation, t::setInvocation, p.anyOf(N2oInvocation.class), null);
        p.anyChildren(e, "in-parameters", t::getInFields, t::setInFields, p.oneOf(AbstractParameter.class)
                .add("param", ObjectSimpleField.class, this::invParameter));
        p.children(e, "out-parameters", "param", t::getOutFields, t::setOutFields, ObjectSimpleField.class, this::outParameter);
        p.element(e, "confirmation-text", t::getConfirmationText, t::setConfirmationText);
        p.element(e, "bulk-confirmation-text", t::getBulkConfirmationText, t::setBulkConfirmationText);
        p.element(e, "fail-text", t::getFailText, t::setFailText);
        p.element(e, "description", t::getDescription, t::setDescription);
        p.element(e, "success-text", t::getSuccessText, t::setSuccessText);
        p.element(e, "note", t::getNote, t::setNote);
        p.anyAttributes(e, t::getExtAttributes, t::setExtAttributes);
        p.child(e, null, "validations", t::getValidations, t::setValidations, N2oObject.Operation.Validations.class, this::actionValidations);
        p.read(e, t, (element, entity) -> {
            if (entity.getValidations() == null) {
                entity.setValidations(new N2oObject.Operation.Validations());
                entity.getValidations().setActivate(N2oObject.Operation.Validations.Activate.all);
            }
        });
    }

    private void actionValidations(Element e, N2oObject.Operation.Validations t, IOProcessor p) {
        p.attributeEnum(e, "activate", t::getActivate, t::setActivate, N2oObject.Operation.Validations.Activate.class);
        p.children(e, null, "validation", t::getRefValidations, t::setRefValidations,
                N2oObject.Operation.Validations.Validation.class, this::actionValidation);
    }

    private void actionValidation(Element e, N2oObject.Operation.Validations.Validation t, IOProcessor p) {
        p.attribute(e, "ref-id", t::getRefId, t::setRefId);
    }

    private void abstractParameter(Element e, AbstractParameter t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "mapping", t::getMapping, t::setMapping);
        p.attributeBoolean(e, "required", t::getRequired, t::setRequired);
    }

    private void constraint(Element e, N2oConstraint t, IOProcessor p) {
        validation(e, t, p);
        p.childAttribute(e, "result", "expression", t::getResult, t::setResult);
        p.anyChildren(e, "in-parameters", t::getInFields, t::setInFields, p.oneOf(AbstractParameter.class)
                .add("param", ObjectSimpleField.class, this::invParameter));
        p.children(e, "out-parameters", "param", t::getOutFields, t::setOutFields, ObjectSimpleField.class, this::outParameter);
        p.anyChild(e, "invocation", t::getN2oInvocation, t::setN2oInvocation, p.anyOf(N2oInvocation.class), null);
    }

    private void condition(Element e, N2oValidationCondition t, IOProcessor p) {
        validation(e, t, p);
        p.element(e, "expression", t::getExpression, t::setExpression);
        p.childAttribute(e, "expression", "on", t::getExpressionOn, t::setExpressionOn);
    }

    private void invParameter(Element e, ObjectSimpleField t, IOProcessor p) {
//        abstractParameter(e, t, p); todo нельзя использовать из-за конфликта между id и name
        parameter(e, t, p);
    }

    private void outParameter(Element e, ObjectSimpleField t, IOProcessor p) {
//        abstractParameter(e, t, p); todo нельзя использовать из-за конфликта между id и name
        parameter(e, t, p);
    }

    private void parameter(Element e, ObjectSimpleField t, IOProcessor p) {
        p.attribute(e, "name", t::getId, t::setId);
        p.attribute(e, "mapping", t::getMapping, t::setMapping);
        p.attributeBoolean(e, "required", t::getRequired, t::setRequired);
        p.attribute(e, "value", t::getDefaultValue, t::setDefaultValue);
        p.attribute(e, "domain", t::getDomain, t::setDomain);
        p.attribute(e, "normalizer", t::getNormalize, t::setNormalize);
    }

    private void field(Element e, ObjectSimpleField t, IOProcessor p) {
        abstractParameter(e, t, p);
        p.attribute(e, "domain", t::getDomain, t::setDomain);
    }

    private void referenceField(Element e, ObjectReferenceField t, IOProcessor p) {
        abstractParameter(e, t, p);
        p.attribute(e, "reference-object-id", t::getReferenceObjectId, t::setReferenceObjectId);
    }

    private void validation(Element e, N2oValidation t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attributeEnum(e, "level", t::getLevel, t::setLevel, N2oValidation.Level.class);
        p.attribute(e, "moment", t::getMoment, t::setMoment);
        p.element(e, "message", t::getMessage, t::setMessage);
    }

    @Override
    public Class<N2oObject> getElementClass() {
        return N2oObject.class;
    }

    @Override
    public N2oObject newInstance(Element element) {
        return new N2oObject();
    }

    @Override
    public String getElementName() {
        return "object";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/object-2.0";
    }

}

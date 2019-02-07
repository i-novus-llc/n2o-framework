package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.MapperType;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oMandatory;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidationCondition;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.dataprovider.DataProviderIOv1;
import net.n2oapp.framework.config.io.toolbar.ToolbarIO;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Чтение/запись базовых свойств поля
 */
public abstract class FieldIOv2<T extends N2oField> extends ComponentIO<T> implements ControlIOv2 {

    private static Namespace dataProviderNamespace = DataProviderIOv1.NAMESPACE;

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "required", m::getRequired, m::setRequired);
        p.attributeBoolean(e, "visible", m::getVisible, m::setVisible);
        p.attributeBoolean(e, "enabled", m::getEnabled, m::setEnabled);
        p.child(e, null, "toolbar", m::getToolbar, m::setToolbar, new ToolbarIO());
        p.anyChildren(e, "dependencies", m::getDependencies, m::setDependencies, p.oneOf(N2oField.Dependency.class)
                .add("enabling", N2oField.EnablingDependency.class, this::dependency)
                .add("visibility", N2oField.VisibilityDependency.class, this::visibilityDependency)
                .add("requiring", N2oField.RequiringDependency.class, this::dependency)
                .add("set-value", N2oField.SetValueDependency.class, this::dependency));
        p.child(e, null, "validations", m::getValidations, m::setValidations,
                N2oField.Validations.class, this::inlineValidations);
        p.attributeArray(e, "depends-on", ",", m::getDependsOn, m::setDependsOn);
    }

    private void dependency(Element e, N2oField.Dependency t, IOProcessor p) {
        p.attribute(e, "on", t::getOn, t::setOn);
        p.text(e, t::getValue, t::setValue);
    }

    private void visibilityDependency(Element e, N2oField.VisibilityDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attributeBoolean(e, "reset", t::getReset, t::setReset);
    }

    private void inlineValidations(Element e, N2oField.Validations t, IOProcessor p) {
        p.attributeArray(e, "white-list", ",", t::getWhiteList, t::setWhiteList);
        p.anyChildren(e, null, t::getInlineValidations, t::setInlineValidations, p.oneOf(N2oValidation.class)
                .add("constraint", N2oConstraint.class, this::constraint)
                .add("condition", N2oValidationCondition.class, this::condition)
                .add("mandatory", N2oMandatory.class, this::mandatory));
    }

    private void validation(Element e, N2oValidation t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attributeEnum(e, "severity", t::getSeverity, t::setSeverity, SeverityType.class);
        p.attributeEnum(e, "client-moment", t::getClientMoment, t::setClientMoment, N2oValidation.ClientMoment.class);
        p.attributeEnum(e, "server-moment", t::getServerMoment, t::setServerMoment, N2oValidation.ServerMoment.class);
        p.attribute(e, "field-id", t::getFieldId, t::setFieldId);
        p.attribute(e, "message", t::getMessage, t::setMessage);
        p.attribute(e, "enabled", t::getEnabled, t::setEnabled);
        p.attribute(e, "side", t::getSide, t::setSide);
    }

    private void constraint(Element e, N2oConstraint t, IOProcessor p) {
        validation(e, t, p);
        p.childrenText(e, "result", t::getResult, t::setResult);
        p.childAttributeEnum(e, "result", "mapper", t::getMapper, t::setMapper, MapperType.class);
        p.children(e, "in-parameters", "param", t::getInParameters, t::setInParameters, N2oObject.Parameter.class, this::param);
        p.children(e, "out-parameters", "param", t::getOutParameters, t::setOutParameters, N2oObject.Parameter.class, this::param);
        p.anyChild(e, "invocation", t::getN2oInvocation, t::setN2oInvocation, p.anyOf(N2oInvocation.class), dataProviderNamespace);
    }

    private void condition(Element e, N2oValidationCondition t, IOProcessor p) {
        validation(e, t, p);
        p.text(e, t::getExpression, t::setExpression);
        p.attribute(e, "on", t::getExpressionOn, t::setExpressionOn);
        p.attribute(e, "src", t::getSrc, t::setSrc);
    }

    private void mandatory(Element e, N2oMandatory t, IOProcessor p) {
        validation(e, t, p);
        p.text(e, t::getExpression, t::setExpression);
        p.attribute(e, "on", t::getExpressionOn, t::setExpressionOn);
        p.attribute(e, "src", t::getSrc, t::setSrc);
    }

    private void param(Element e, N2oObject.Parameter t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attribute(e, "default-value", t::getDefaultValue, t::setDefaultValue);
        p.attribute(e, "domain", t::getDomain, t::setDomain);
        p.attribute(e, "normalize", t::getNormalize, t::setNormalize);
        p.attributeEnum(e, "mapper", t::getMapper, t::setMapper, MapperType.class);
        p.attribute(e, "mapping", t::getMapping, t::setMapping);
        p.attributeBoolean(e, "required", t::getRequired, t::setRequired);
        p.attribute(e, "mapping-condition", t::getMappingCondition, t::setMappingCondition);
        p.attribute(e, "entity-class", t::getEntityClass, t::setEntityClass);
        p.children(e, null, "child-param", t::getChildParams, t::setChildParams, N2oObject.Parameter.class, this::param);
    }
}

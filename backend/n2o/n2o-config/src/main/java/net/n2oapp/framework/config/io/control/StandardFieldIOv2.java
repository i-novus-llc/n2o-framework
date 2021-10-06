package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.MapperType;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oMandatory;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidationCondition;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.config.io.dataprovider.DataProviderIOv1;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение/запись базовых свойств контрола
 */
public abstract class StandardFieldIOv2<T extends N2oStandardField> extends FieldIOv2<T> {

    private static Namespace dataProviderNamespace = DataProviderIOv1.NAMESPACE;

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "placeholder", m::getPlaceholder, m::setPlaceholder);
        p.attributeBoolean(e, "copied", m::getCopied, m::setCopied);
        p.child(e, null, "validations", m::getValidations, m::setValidations,
                N2oField.Validations.class, this::inlineValidations);
        p.child(e, null, "submit", m::getSubmit, m::setSubmit, Submit.class, this::submit);
    }

    private void inlineValidations(Element e, N2oField.Validations t, IOProcessor p) {
        p.attributeArray(e, "white-list", ",", t::getWhiteList, t::setWhiteList);
        p.anyChildren(e, null, t::getInlineValidations, t::setInlineValidations, p.oneOf(N2oValidation.class)
                .add("constraint", N2oConstraint.class, this::constraint)
                .add("condition", N2oValidationCondition.class, this::condition)
                .add("mandatory", N2oMandatory.class, this::mandatory));
    }

    private void constraint(Element e, N2oConstraint t, IOProcessor p) {
        validation(e, t, p);
        p.attribute(e, "result", t::getResult, t::setResult);
        p.childAttributeEnum(e, "result", "mapper", t::getMapper, t::setMapper, MapperType.class);
        p.anyChildren(e, "in", t::getInFields, t::setInFields, p.oneOf(AbstractParameter.class)
                .add("field", ObjectSimpleField.class, this::param));
        if (t.getInFields() == null)
            p.anyChildren(e, "in-parameters", t::getInFields, t::setInFields, p.oneOf(AbstractParameter.class)
                    .add("param", ObjectSimpleField.class, this::param));
        p.children(e, "out", "field", t::getOutFields, t::setOutFields, ObjectSimpleField.class, this::param);
        if (t.getOutFields() == null)
            p.children(e, "out-parameters", "param", t::getOutFields, t::setOutFields, ObjectSimpleField.class, this::param);
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

    private void validation(Element e, N2oValidation t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attributeEnum(e, "severity", t::getSeverity, t::setSeverity, SeverityType.class);
        p.attributeEnum(e, "server-moment", t::getServerMoment, t::setServerMoment, N2oValidation.ServerMoment.class);
        p.attribute(e, "field-id", t::getFieldId, t::setFieldId);
        p.attribute(e, "message", t::getMessage, t::setMessage);
        p.attribute(e, "enabled", t::getEnabled, t::setEnabled);
        if (t.getEnabled() == null)
            p.attribute(e, "mapping-condition", t::getEnabled, t::setEnabled);
        p.attribute(e, "side", t::getSide, t::setSide);
    }

    private void submit(Element e, Submit t, IOProcessor p) {
        p.attribute(e, "operation-id", t::getOperationId, t::setOperationId);
        p.attributeBoolean(e, "message-on-success", t::getMessageOnSuccess, t::setMessageOnSuccess);
        p.attributeBoolean(e, "message-on-fail", t::getMessageOnFail, t::setMessageOnFail);
        p.attributeEnum(e, "message-position", t::getMessagePosition, t::setMessagePosition, MessagePosition.class);
        p.attributeEnum(e, "message-placement", t::getMessagePlacement, t::setMessagePlacement, MessagePlacement.class);
        p.attributeBoolean(e, "refresh-on-success", t::getRefreshOnSuccess, t::setRefreshOnSuccess);
        p.attribute(e, "refresh-widget-id", t::getRefreshWidgetId, t::setRefreshWidgetId);
        p.attribute(e, "route", t::getRoute, t::setRoute);
        p.children(e, null, "path-param", t::getPathParams, t::setPathParams, N2oParam.class, this::submitParam);
        p.children(e, null, "header-param", t::getHeaderParams, t::setHeaderParams, N2oParam.class, this::submitParam);
        p.children(e, null, "form-param", t::getFormParams, t::setFormParams, N2oFormParam.class, this::submitFormParam);
    }

    private void submitParam(Element e, N2oParam t, IOProcessor p) {
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "value", t::getValue, t::setValue);
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getRefModel, t::setRefModel, ReduxModel.class);
    }

    private void submitFormParam(Element e, N2oFormParam t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        if (t.getId() == null)
            p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "value", t::getValue, t::setValue);
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getRefModel, t::setRefModel, ReduxModel.class);
    }

    private void param(Element e, ObjectSimpleField t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attribute(e, "default-value", t::getDefaultValue, t::setDefaultValue);
        p.attribute(e, "domain", t::getDomain, t::setDomain);
        p.attribute(e, "normalize", t::getNormalize, t::setNormalize);
        p.attribute(e, "mapping", t::getMapping, t::setMapping);
        p.attributeBoolean(e, "required", t::getRequired, t::setRequired);
        p.attribute(e, "enabled", t::getEnabled, t::setEnabled);
        if (t.getEnabled() == null)
            p.attribute(e, "mapping-condition", t::getEnabled, t::setEnabled);
    }
}

package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.PageRefEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ComponentIO;
import net.n2oapp.framework.config.io.toolbar.v2.FieldToolbarIOv2;
import org.jdom2.Element;

/**
 * Чтение/запись базовых свойств поля
 */
public abstract class FieldIOv3<T extends N2oField> extends ComponentIO<T> implements ControlIOv3 {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "required", m::getRequired, m::setRequired);
        p.attribute(e, "visible", m::getVisible, m::setVisible);
        p.attribute(e, "enabled", m::getEnabled, m::setEnabled);
        p.attribute(e, "label", m::getLabel, m::setLabel);
        p.attribute(e, "label-class", m::getLabelClass, m::setLabelClass);
        p.attribute(e, "no-label", m::getNoLabel, m::setNoLabel);
        p.attribute(e, "no-label-block", m::getNoLabelBlock, m::setNoLabelBlock);
        p.attribute(e, "description", m::getDescription, m::setDescription);
        p.attribute(e, "domain", m::getDomain, m::setDomain);
        p.attribute(e, "param", m::getParam, m::setParam);
        p.attribute(e, "help", m::getHelp, m::setHelp);
        p.child(e, null, "toolbar", m::getToolbar, m::setToolbar, new FieldToolbarIOv2());
        p.anyChildren(e, "dependencies", m::getDependencies, m::setDependencies, p.oneOf(N2oField.Dependency.class)
                .add("enabling", N2oField.EnablingDependency.class, this::enablingDependency)
                .add("visibility", N2oField.VisibilityDependency.class, this::visibilityDependency)
                .add("requiring", N2oField.RequiringDependency.class, this::requiringDependency)
                .add("set-value", N2oField.SetValueDependency.class, this::setValueDependency)
                .add("reset", N2oField.ResetDependency.class, this::resetDependency)
                .add("fetch", N2oField.FetchDependency.class, this::dependency)
                .add("fetch-value", N2oField.FetchValueDependency.class, this::fetchValueDependency));
        p.attributeArray(e, "depends-on", ",", m::getDependsOn, m::setDependsOn);
        p.attributeEnum(e, "ref-model", m::getRefModel, m::setRefModel, ReduxModelEnum.class);
        p.attributeEnum(e, "ref-page", m::getRefPage, m::setRefPage, PageRefEnum.class);
        p.attribute(e, "ref-datasource", m::getRefDatasourceId, m::setRefDatasourceId);
        p.attribute(e, "ref-field-id", m::getRefFieldId, m::setRefFieldId);
    }

    private void dependency(Element e, N2oField.Dependency t, IOProcessor p) {
        p.attributeArray(e, "on", ",", t::getOn, t::setOn);
        p.attributeBoolean(e, "apply-on-init", t::getApplyOnInit, t::setApplyOnInit);
        p.text(e, t::getValue, t::setValue);
    }

    private void enablingDependency(Element e, N2oField.EnablingDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attribute(e, "message", t::getMessage, t::setMessage);
    }

    private void visibilityDependency(Element e, N2oField.VisibilityDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attributeBoolean(e, "reset", t::getReset, t::setReset);
    }

    private void requiringDependency(Element e, N2oField.RequiringDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attributeBoolean(e, "validate", t::getValidate, t::setValidate);
    }

    private void setValueDependency(Element e, N2oField.SetValueDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attributeBoolean(e, "validate", t::getValidate, t::setValidate);
    }

    private void resetDependency(Element e, N2oField.ResetDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attributeBoolean(e, "validate", t::getValidate, t::setValidate);
    }

    private void fetchValueDependency(Element e, N2oField.FetchValueDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attribute(e, "query-id", t::getQueryId, t::setQueryId);
        p.attribute(e, "value-field-id", t::getValueFieldId, t::setValueFieldId);
        p.attributeInteger(e, "size", t::getSize, t::setSize);
        p.childrenByEnum(e, "filters", t::getPreFilters, t::setPreFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterTypeEnum.class, this::prefilter);
    }

    protected void prefilter(Element e, N2oPreFilter pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "value", pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.attributeBoolean(e, "required", pf::getRequired, pf::setRequired);
        p.attributeBoolean(e, "reset-on-change", pf::getResetOnChange, pf::setResetOnChange);
        p.attribute(e, "datasource", pf::getDatasourceId, pf::setDatasourceId);
        p.attributeEnum(e, "model", pf::getModel, pf::setModel, ReduxModelEnum.class);
        p.childrenToStringArray(e, null, "value", pf::getValueList, pf::setValueList);
        p.attribute(e, "param", pf::getParam, pf::setParam);
    }

}

package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.toolbar.ToolbarIO;
import org.jdom.Element;

/**
 * Чтение/запись базовых свойств поля
 */
public abstract class FieldIOv2<T extends N2oField> extends ComponentIO<T> implements ControlIOv2 {


    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "id", m::getId, m::setId);
        p.attributeBoolean(e, "required", m::getRequired, m::setRequired);
        p.attributeBoolean(e, "visible", m::getVisible, m::setVisible);
        p.attributeBoolean(e, "enabled", m::getEnabled, m::setEnabled);
        p.child(e, null, "toolbar", m::getToolbar, m::setToolbar, new ToolbarIO());
        p.anyChildren(e, "dependencies", m::getDependencies, m::setDependencies, p.oneOf(N2oField.Dependency.class)
                .add("enabling", N2oField.EnablingDependency.class, this::dependency)
                .add("visibility", N2oField.VisibilityDependency.class, this::visibilityDependency)
                .add("requiring", N2oField.RequiringDependency.class, this::dependency)
                .add("set-value", N2oField.SetValueDependency.class, this::dependency)
                .add("fetch", N2oField.FetchDependency.class, this::dependency));
        p.attributeArray(e, "depends-on", ",", m::getDependsOn, m::setDependsOn);
    }

    private void dependency(Element e, N2oField.Dependency t, IOProcessor p) {
        p.attributeArray(e, "on", ",", t::getOn, t::setOn);
        p.attributeBoolean(e, "apply-on-init", t::getApplyOnInit, t::setApplyOnInit);
        p.text(e, t::getValue, t::setValue);
    }

    private void visibilityDependency(Element e, N2oField.VisibilityDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attributeBoolean(e, "reset", t::getReset, t::setReset);
    }

}

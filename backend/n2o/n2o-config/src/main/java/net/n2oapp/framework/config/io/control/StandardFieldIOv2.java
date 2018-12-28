package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;

/**
 * Чтение/запись базовых свойств контрола
 */
public abstract class StandardFieldIOv2<T extends N2oStandardField> extends FieldIOv2<T>{

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "label", m::getLabel, m::setLabel);
        p.attribute(e, "label-class", m::getLabelClass, m::setLabelClass);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "placeholder", m::getPlaceholder, m::setPlaceholder);
        p.attribute(e, "description", m::getDescription, m::setDescription);
        p.attribute(e, "domain", m::getDomain, m::setDomain);
        p.attributeBoolean(e, "copied", m::getCopied, m::setCopied);
        p.attribute(e, "help", m::getHelp, m::setHelp);
    }
}

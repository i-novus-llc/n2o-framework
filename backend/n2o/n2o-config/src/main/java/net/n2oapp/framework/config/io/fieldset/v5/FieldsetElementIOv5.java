package net.n2oapp.framework.config.io.fieldset.v5;

import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.global.view.fieldset.FieldLabelAlignEnum;
import net.n2oapp.framework.api.metadata.global.view.fieldset.FieldLabelLocationEnum;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import org.jdom2.Element;

/**
 * Чтение\запись филдсета версии 5.0
 */
public abstract class FieldsetElementIOv5<T extends N2oFieldSet> implements NamespaceIO<T>, BadgeAwareIO<N2oFieldSet> {

    @Override
    public void io(Element e, T fs, IOProcessor p) {
        p.attribute(e, "id", fs::getId, fs::setId);
        p.attribute(e, "ref-id", fs::getRefId, fs::setRefId);
        p.attribute(e, "src", fs::getSrc, fs::setSrc);
        p.attribute(e, "class", fs::getCssClass, fs::setCssClass);
        p.attribute(e, "style", fs::getStyle, fs::setStyle);
        p.attribute(e, "label", fs::getLabel, fs::setLabel);
        p.attribute(e, "description", fs::getDescription, fs::setDescription);
        p.attributeEnum(e, "field-label-location", fs::getFieldLabelLocation, fs::setFieldLabelLocation, FieldLabelLocationEnum.class);
        p.attributeEnum(e, "field-label-align", fs::getFieldLabelAlign, fs::setFieldLabelAlign, FieldLabelAlignEnum.class);
        p.attribute(e, "field-label-width", fs::getFieldLabelWidth, fs::setFieldLabelWidth);
        p.attribute(e, "visible", fs::getVisible, fs::setVisible);
        p.attribute(e, "enabled", fs::getEnabled, fs::setEnabled);
        p.attribute(e, "help", fs::getHelp, fs::setHelp);
        p.attributeArray(e, "depends-on", ",", fs::getDependsOn, fs::setDependsOn);
        p.anyChildren(e, null, fs::getItems, fs::setItems, p.anyOf(FieldsetItem.class), FieldsetIOv5.NAMESPACE, ControlIOv3.NAMESPACE);
        p.anyAttributes(e, fs::getExtAttributes, fs::setExtAttributes);
        badge(e, fs, p);
    }


    @Override
    public String getNamespaceUri() {
        return FieldsetIOv5.NAMESPACE.getURI();
    }
}

package net.n2oapp.framework.config.io.fieldset.v5;


import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись филдсета версии 5.0
 */
public abstract class FieldsetElementIOv5<T extends N2oFieldSet> implements NamespaceIO<T> {
    private static final Namespace fieldsetDefaultNamespace = FieldsetIOv5.NAMESPACE;
    private static final Namespace controlDefaultNamespace = ControlIOv3.NAMESPACE;

    @Override
    public void io(Element e, T fs, IOProcessor p) {
        p.attribute(e, "id", fs::getId, fs::setId);
        p.attribute(e, "ref-id", fs::getRefId, fs::setRefId);
        p.attribute(e, "src", fs::getSrc, fs::setSrc);
        p.attribute(e, "class", fs::getCssClass, fs::setCssClass);
        p.attribute(e, "style", fs::getStyle, fs::setStyle);
        p.attribute(e, "label", fs::getLabel, fs::setLabel);
        p.attribute(e, "description", fs::getDescription, fs::setDescription);
        p.attributeEnum(e, "field-label-location", fs::getFieldLabelLocation, fs::setFieldLabelLocation, N2oFieldSet.FieldLabelLocation.class);
        p.attributeEnum(e, "field-label-align", fs::getFieldLabelAlign, fs::setFieldLabelAlign, N2oFieldSet.FieldLabelAlign.class);
        p.attribute(e, "field-label-width", fs::getFieldLabelWidth, fs::setFieldLabelWidth);
        p.attribute(e, "enabled", fs::getEnabled, fs::setEnabled);
        p.attribute(e, "visible", fs::getVisible, fs::setVisible);
        p.attribute(e, "help", fs::getHelp, fs::setHelp);
        p.attributeArray(e, "depends-on", ",", fs::getDependsOn, fs::setDependsOn);
        p.anyChildren(e, null, fs::getItems, fs::setItems, p.anyOf(SourceComponent.class), fieldsetDefaultNamespace, controlDefaultNamespace);
        p.anyAttributes(e, fs::getExtAttributes, fs::setExtAttributes);
    }


    @Override
    public String getNamespaceUri() {
        return fieldsetDefaultNamespace.getURI();
    }
}

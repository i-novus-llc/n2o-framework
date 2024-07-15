package net.n2oapp.framework.config.io.fieldset.v4;

import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetCol;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.control.v2.ControlIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ColElementIO4 implements NamespaceIO<N2oFieldsetCol> {
    private static final Namespace DEFAULT_NAMESPACE = FieldsetIOv4.NAMESPACE;
    private static final Namespace controlDefaultNamespace = ControlIOv2.NAMESPACE;

    @Override
    public void io(Element e, N2oFieldsetCol col, IOProcessor p) {
        p.attribute(e, "class", col::getCssClass, col::setCssClass);
        p.attribute(e, "style", col::getStyle, col::setStyle);
        p.attributeInteger(e, "size", col::getSize, col::setSize);
        p.attribute(e, "visible", col::getVisible, col::setVisible);
        p.anyAttributes(e, col::getExtAttributes, col::setExtAttributes);
        p.anyChildren(e, null, col::getItems, col::setItems, p.anyOf(FieldsetItem.class), DEFAULT_NAMESPACE, controlDefaultNamespace);
    }

    @Override
    public String getElementName() {
        return "col";
    }

    @Override
    public String getNamespaceUri() {
        return FieldsetIOv4.NAMESPACE.getURI();
    }

    @Override
    public Class<N2oFieldsetCol> getElementClass() {
        return N2oFieldsetCol.class;
    }
}
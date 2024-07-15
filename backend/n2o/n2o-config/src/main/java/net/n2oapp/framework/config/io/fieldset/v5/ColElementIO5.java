package net.n2oapp.framework.config.io.fieldset.v5;

import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetCol;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента столбца филдсета версии 5.0
 */
@Component
public class ColElementIO5 implements NamespaceIO<N2oFieldsetCol> {

    @Override
    public void io(Element e, N2oFieldsetCol col, IOProcessor p) {
        p.attribute(e, "class", col::getCssClass, col::setCssClass);
        p.attribute(e, "style", col::getStyle, col::setStyle);
        p.attributeInteger(e, "size", col::getSize, col::setSize);
        p.attribute(e, "visible", col::getVisible, col::setVisible);
        p.anyAttributes(e, col::getExtAttributes, col::setExtAttributes);
        p.anyChildren(e, null, col::getItems, col::setItems, p.anyOf(FieldsetItem.class), FieldsetIOv5.NAMESPACE, ControlIOv3.NAMESPACE);
    }

    @Override
    public String getElementName() {
        return "col";
    }

    @Override
    public String getNamespaceUri() {
        return FieldsetIOv5.NAMESPACE.getURI();
    }

    @Override
    public Class<N2oFieldsetCol> getElementClass() {
        return N2oFieldsetCol.class;
    }
}
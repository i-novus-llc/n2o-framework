package net.n2oapp.framework.config.io.fieldset.v5;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение и запись компонента строки филдсета версии 5.0
 */
@Component
public class RowElementIO5 implements NamespaceIO<N2oFieldsetRow> {
    private static final Namespace DEFAULT_NAMESPACE = FieldsetIOv5.NAMESPACE;
    private static final Namespace controlDefaultNamespace = ControlIOv3.NAMESPACE;

    @Override
    public void io(Element e, N2oFieldsetRow row, IOProcessor p) {
        p.attribute(e, "class", row::getCssClass, row::setCssClass);
        p.attribute(e, "style", row::getStyle, row::setStyle);
        p.anyChildren(e, null, row::getItems, row::setItems, p.anyOf(SourceComponent.class), DEFAULT_NAMESPACE, controlDefaultNamespace);
    }

    @Override
    public String getElementName() {
        return "row";
    }

    @Override
    public String getNamespaceUri() {
        return FieldsetIOv5.NAMESPACE.getURI();
    }

    @Override
    public Class<N2oFieldsetRow> getElementClass() {
        return N2oFieldsetRow.class;
    }
}
package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oFooter;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ComponentIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись подвала (footer)
 */
@Component
public class FooterIO extends ComponentIO<N2oFooter> {

    @Override
    public String getElementName() {
        return "footer";
    }

    @Override
    public Class<N2oFooter> getElementClass() {
        return N2oFooter.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/application-1.0";
    }

    @Override
    public void io(Element e, N2oFooter m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "visible", m::getVisible, m::setVisible);
        p.attribute(e, "right-text", m::getRightText, m::setRightText);
        p.attribute(e, "left-text", m::getLeftText, m::setLeftText);
    }


}

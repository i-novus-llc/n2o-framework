package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия страницы поверх текущей версии 2.0
 */
@Component
public class OpenPageElementIOV2 extends AbstractOpenPageElementIOV2<N2oOpenPage> {
    @Override
    public void io(Element e, N2oOpenPage op, IOProcessor p) {
        super.io(e, op, p);
    }

    @Override
    public String getElementName() {
        return "open-page";
    }

    @Override
    public Class<N2oOpenPage> getElementClass() {
        return N2oOpenPage.class;
    }
}

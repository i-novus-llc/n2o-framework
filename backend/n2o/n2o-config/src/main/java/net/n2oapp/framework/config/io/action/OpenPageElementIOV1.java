package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия старницы поверх текущей
 */
@Component
public class OpenPageElementIOV1 extends AbstractOpenPageElementIOV1<N2oOpenPage> {
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

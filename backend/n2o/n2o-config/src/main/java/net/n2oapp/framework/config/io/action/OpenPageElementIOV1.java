package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.action.N2oOpenPage;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия старницы поверх текущей
 */
@Component
public class OpenPageElementIOV1 extends AbstractOpenPageElementIOV1<N2oOpenPage> {

    @Override
    public String getElementName() {
        return "open-page";
    }

    @Override
    public Class<N2oOpenPage> getElementClass() {
        return N2oOpenPage.class;
    }
}

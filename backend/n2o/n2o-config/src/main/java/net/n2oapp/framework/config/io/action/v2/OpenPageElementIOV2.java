package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.action.N2oOpenPage;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия страницы поверх текущей версии 2.0
 */
@Component
public class OpenPageElementIOV2 extends AbstractOpenPageElementIOV2<N2oOpenPage> {
    @Override
    public String getElementName() {
        return "open-page";
    }

    @Override
    public Class<N2oOpenPage> getElementClass() {
        return N2oOpenPage.class;
    }
}

package net.n2oapp.framework.config.io.control.v2.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента вывода html
 */
@Component
public class HtmlIOv2 extends PlainFieldIOv2<N2oHtml> {

    @Override
    public Class<N2oHtml> getElementClass() {
        return N2oHtml.class;
    }

    @Override
    public String getElementName() {
        return "html";
    }
}

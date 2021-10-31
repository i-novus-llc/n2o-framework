package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oHidden;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.plain.PlainFieldIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента hidden (Скрытый компонент ввода)
 */
@Component
public class HiddenIOv2 extends PlainFieldIOv2<N2oHidden> {

    @Override
    public void io(Element e, N2oHidden m, IOProcessor p) {
        super.io(e, m, p);
    }

    @Override
    public Class<N2oHidden> getElementClass() {
        return N2oHidden.class;
    }

    @Override
    public String getElementName() {
        return "hidden";
    }
}

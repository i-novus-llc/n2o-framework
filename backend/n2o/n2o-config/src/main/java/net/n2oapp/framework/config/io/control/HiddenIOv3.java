package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oHidden;
import net.n2oapp.framework.config.io.control.plain.PlainFieldIOv3;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента hidden (Скрытый компонент ввода) версии 3.0
 */
@Component
public class HiddenIOv3 extends PlainFieldIOv3<N2oHidden> {
    @Override
    public Class<N2oHidden> getElementClass() {
        return N2oHidden.class;
    }

    @Override
    public String getElementName() {
        return "hidden";
    }
}

package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oCustomControl;
import org.springframework.stereotype.Component;

/**
 * Чтение запись настраиваемого компонента ввода
 */
@Component
public class CustomControlIOv2 extends ComponentIO<N2oCustomControl> implements ControlIOv2 {

    @Override
    public Class<N2oCustomControl> getElementClass() {
        return N2oCustomControl.class;
    }

    @Override
    public String getElementName() {
        return "control";
    }
}

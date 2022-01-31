package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oCustomControl;
import net.n2oapp.framework.config.io.control.ComponentIO;
import org.springframework.stereotype.Component;

/**
 * Чтение запись настраиваемого компонента ввода версии 3.0
 */
@Component
public class CustomControlIOv3 extends ComponentIO<N2oCustomControl> implements ControlIOv3 {

    @Override
    public Class<N2oCustomControl> getElementClass() {
        return N2oCustomControl.class;
    }

    @Override
    public String getElementName() {
        return "control";
    }
}

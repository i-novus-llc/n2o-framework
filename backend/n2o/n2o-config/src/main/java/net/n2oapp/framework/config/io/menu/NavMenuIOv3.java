package net.n2oapp.framework.config.io.menu;

import org.springframework.stereotype.Component;

/**
 * Чтение/запись основного меню 3.0
 */
@Component
public class NavMenuIOv3 extends SimpleMenuIOv3 {

    @Override
    public String getElementName() {
        return "nav";
    }
}

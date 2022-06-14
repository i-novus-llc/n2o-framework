package net.n2oapp.framework.config.io.menu;

import org.springframework.stereotype.Component;

/**
 * Чтение/запись основного меню 2.0
 */
@Component
public class NavMenuIOv2 extends SimpleMenuIOv2 {

    @Override
    public String getElementName() {
        return "nav";
    }
}

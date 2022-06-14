package net.n2oapp.framework.config.io.menu;

import org.springframework.stereotype.Component;

/*
 * Чтение/запись дополнительного меню 3.0
 */
@Component
public class ExtraMenuIOv3 extends SimpleMenuIOv3 {

    @Override
    public String getElementName() {
        return "extra-menu";
    }
}

package net.n2oapp.framework.autotest;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.component.page.Page;
import net.n2oapp.framework.autotest.factory.ComponentFactory;

import static com.codeborne.selenide.Selenide.$;


public class N2oSelenide {
    private static ComponentFactory factory = new ComponentFactory();

    public static <T extends Page> T open(String relativeOrAbsoluteUrl, Class<T> componentClass) {
        Selenide.open(relativeOrAbsoluteUrl);
        return factory.component($("body"), componentClass);
    }

    public static <T extends Page> T page(Class<T> componentClass) {
        return factory.component($("body"), componentClass);
    }

    static ComponentFactory factory() {
        factory = new ComponentFactory();
        return factory;
    }
}

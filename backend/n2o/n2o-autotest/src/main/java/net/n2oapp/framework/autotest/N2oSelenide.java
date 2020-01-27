package net.n2oapp.framework.autotest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.impl.N2oComponentLibrary;

import static com.codeborne.selenide.Selenide.$;

/**
 * Главный класс для старта автотестирования страниц N2O
 */
public class N2oSelenide {
    private static ComponentFactory factory = new ComponentFactory().addLibrary(new N2oComponentLibrary());

    public static <T extends Page> T open(String relativeOrAbsoluteUrl, Class<T> pageClass) {
        Selenide.open(relativeOrAbsoluteUrl);
        return page(pageClass);
    }

    public static <T extends Page> T page(Class<T> pageClass) {
        return factory.produce($("body"), pageClass);
    }

    public static <T extends Component> T component(SelenideElement element, Class<T> componentClass) {
        return factory.produce(element, componentClass);
    }

    public static <T extends ComponentsCollection> T collection(ElementsCollection elements, Class<T> collectionClass) {
        return factory.produce(elements, collectionClass);
    }

    public static void setFactory(ComponentFactory newFactory) {
        factory = newFactory;
    }
}

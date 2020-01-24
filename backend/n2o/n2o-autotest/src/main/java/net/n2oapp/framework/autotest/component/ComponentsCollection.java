package net.n2oapp.framework.autotest.component;

import com.codeborne.selenide.ElementsCollection;

public interface ComponentsCollection {

    ElementsCollection elements();
    void setElements(ElementsCollection elements);
}

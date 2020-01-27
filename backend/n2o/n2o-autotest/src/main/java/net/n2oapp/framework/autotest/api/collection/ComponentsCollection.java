package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.ElementsCollection;

public interface ComponentsCollection {
    ElementsCollection elements();
    void setElements(ElementsCollection elements);

    void shouldHaveSize(int size);
    void shouldBeEmpty();
}

package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;

/**
 * Абстрактная реализация коллекции компонентов для автотестирования
 */
public abstract class N2oComponentsCollection implements ComponentsCollection {
    private ElementsCollection elements;

    public ElementsCollection elements() {
        return elements;
    }

    @Override
    public void setElements(ElementsCollection elements) {
        this.elements = elements;
    }

    @Override
    public void shouldHaveSize(int size) {
        elements().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void shouldBeEmpty() {
        elements().shouldBe(CollectionCondition.empty);
    }
}

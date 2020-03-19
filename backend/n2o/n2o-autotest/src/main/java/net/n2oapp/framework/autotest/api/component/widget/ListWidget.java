package net.n2oapp.framework.autotest.api.component.widget;

public interface ListWidget extends StandardWidget {

    Content content(int index);

    void shouldHaveSize(int size);

    interface Content {
        void bodyShouldHaveText(String text);
    }
}

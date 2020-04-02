package net.n2oapp.framework.autotest.api.component.widget.list;

import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

public interface ListWidget extends StandardWidget {

    Content content(int index);

    void shouldHaveSize(int size);

    Paging paging();

    interface Content {

        void click();

        <T extends Cell> T leftTop(Class<T> clazz);

        <T extends Cell> T leftBottom(Class<T> clazz);

        <T extends Cell> T header(Class<T> clazz);

        <T extends Cell> T body(Class<T> clazz);

        <T extends Cell> T subHeader(Class<T> clazz);

        <T extends Cell> T rightTop(Class<T> clazz);

        <T extends Cell> T rightBottom(Class<T> clazz);

        <T extends Cell> T extra(Class<T> clazz);
    }

    interface Paging {
        void clickPrev();

        void clickNext();

        void totalElementsShouldBe(int count);
    }
}

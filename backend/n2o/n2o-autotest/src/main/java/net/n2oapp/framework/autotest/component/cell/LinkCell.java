package net.n2oapp.framework.autotest.component.cell;

public interface LinkCell extends Cell {
    void click();
    void hrefShouldHave(String href);
    void textShouldHave(String text);
}

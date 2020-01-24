package net.n2oapp.framework.autotest.component.cell;

import net.n2oapp.framework.autotest.Colors;

public interface BadgeCell extends Cell {
    void colorShouldBe(Colors color);
    void textShouldHave(String text);
}

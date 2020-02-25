package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.control.Control;

public interface EditCell extends Cell {

    <T extends Control> T control(Class<T> componentClass);

    void click();

}

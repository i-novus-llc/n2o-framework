package net.n2oapp.framework.autotest.factory;

import net.n2oapp.framework.autotest.component.cell.Cell;
import net.n2oapp.framework.autotest.component.cell.TextCell;

public class Cells {
    public TextCell cell(int index) {
        return null;
    }

    public <T extends Cell> T cell(int index, Class<T> componentClass) {
        return null;
    }
}

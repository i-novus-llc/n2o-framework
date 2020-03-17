package net.n2oapp.framework.autotest.api.component.cell;

public interface CheckboxCell extends Cell {
    boolean isChecked();

    void setChecked(boolean val);

    void shouldBeChecked();

    void shouldBeUnchecked();
}

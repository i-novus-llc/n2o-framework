package net.n2oapp.framework.autotest.api.component.header;

/**
 * Кнопка с выпадающим списком для автотестирования
 */
public interface DropdownMenuItem extends MenuItem {
    AnchorMenuItem item(int index);
}

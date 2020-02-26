package net.n2oapp.framework.autotest.api.component.header;

/**
 * Кнопка с выпадающим списком для автотестирования
 */
public interface Dropdown extends MenuItem {
    Link item(int index);
}

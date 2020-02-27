package net.n2oapp.framework.autotest.api.component.header;

/**
 * Кнопка с ссылкой для автотестирования
 */
public interface AnchorMenuItem extends MenuItem {
    void urlShouldHave(String url);
}

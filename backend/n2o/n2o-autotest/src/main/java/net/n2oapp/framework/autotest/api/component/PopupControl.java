package net.n2oapp.framework.autotest.api.component;

/**
 * Интерфейс компонентов с выпадающим списком
 */
public interface PopupControl extends Element {
    /**
     * Открытие выпадающего списка
     */
    void openPopup();

    /**
     * Закрытие выпадающего списка
     */
    void closePopup();

    /**
     * Проверка того, что выпадающий список раскрыт
     */
    void shouldBeOpened();

    /**
     * Проверка того, что выпадающий список скрыт
     */
    void shouldBeClosed();
}

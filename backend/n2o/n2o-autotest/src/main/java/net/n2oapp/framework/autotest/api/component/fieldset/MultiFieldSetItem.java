package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Элемент филдсета с динамическим числом полей для автотестирования
 */
public interface MultiFieldSetItem extends Component {
    /**
     * Проверка метки на соответствие
     * @param label ожидаемое значение метки
     */
    void shouldHaveLabel(String label);

    /**
     * Проверка наличи кнопки удаления этого филдсета
     */
    void shouldHaveRemoveButton();

    /**
     * Проверка отсутствия кнопки удаления этого филдсета
     */
    void shouldNotHaveRemoveButton();

    /**
     * Клик по кнопке удаления этого филдсета
     */
    void clickRemoveButton();

    /**
     * Проверка наличи кнопки копирования этого филдсета
     */
    void shouldHaveCopyButton();

    /**
     * Проверка наличи кнопки копирования этого филдсета
     */
    void shouldNotHaveCopyButton();

    /**
     * Клик по кнопке копирования этого филдсета
     */
    void clickCopyButton();

    /**
     * Возвращает все поля внутри филдсета
     * @return Поля формы для автотестирования
     */
    Fields fields();

    /**
     * Возвращает филдсеты внутри филдсета
     * @return Филдсеты для автотестирования
     */
    FieldSets fieldsets();
}

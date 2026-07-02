package net.n2oapp.framework.autotest.api.component.widget;

/**
 * Виджет - мульти-форма для автотестирования.
 * <p>
 * Отображает по одной форме на каждую запись модели источника данных,
 * поэтому доступ к полям и кнопкам осуществляется по индексу формы.
 */
public interface MultiFormWidget extends StandardWidget {

    /**
     * Проверка количества отображаемых форм
     *
     * @param size ожидаемое количество форм
     */
    void shouldHaveSize(int size);

    /**
     * @return Компонент пагинации для автотестирования
     */
    Paging paging();


    /**
     * @return Возвращает элемент мульти-формы по индексу
     */
    FormWidget form(int index);
}

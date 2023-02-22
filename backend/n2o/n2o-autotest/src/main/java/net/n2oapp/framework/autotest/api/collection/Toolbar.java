package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

/**
 * Панель действий для автотестирования
 */
public interface Toolbar extends ComponentsCollection {
    /**
     * <p>
     *     Возвращает стандартную кнопку по метке
     * </p>
     *
     * <p>For example: {@code
     *     topLeft().button("Кнопка")
     * }</p>
     *
     * @param label метка кнопки в панели действий
     * @return Компонент стандартная кнопка для автотестирования
     */
    StandardButton button(String label);

    /**
     * <p>
     *     Возвращает стандартную кнопку по условию
     * </p>
     *
     * <p>For example: {@code
     *     topLeft().button("Кнопка")
     * }</p>
     *
     * @param findBy условие поиска
     * @return Компонент стандартная кнопка для автотестирования
     */
    StandardButton button(Condition findBy);

    /**
     * <p>
     *     Возвращает первую найденную кнопку с выпадающим списком
     * </p>
     *
     * <p>For example: {@code
     *     toolbar.dropdown()
     * }</p>
     *
     * @return Компонент кнопка с выпадающим списком для автотестирования
     */
    DropdownButton dropdown();

    /**
     * <p>
     *     Возвращает найденную по условию кнопку с выпадающим списком
     * </p>
     *
     * <p>For example: {@code
     *     toolbar.dropdown(Condition.text("Кнопка"))
     * }</p>
     *
     * @param findBy условие поиска
     * @return Компонент кнопка с выпадающим списком для автотестирования
     */
    DropdownButton dropdown(Condition findBy);

    /**
     * <p>
     *     Возвращает найденную по метке кнопку
     * </p>
     *
     * <p>For example: {@code
     *     toolbar.button("Кнопка", StandardButton.class)
     * }</p>
     *
     * @param label метка кнопки
     * @param componentClass возвращаемый тип
     * @return Компонент кнопка для автотестирования
     */
    <T extends Button> T button(String label, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает найденную по условию кнопку
     * </p>
     *
     * <p>For example: {@code
     *     toolbar.button(Condition.text("Кнопка"), StandardButton.class)
     * }</p>
     *
     * @param findBy условие поиска
     * @param componentClass возвращаемый тип
     * @return Компонент кнопка для автотестирования
     */
    <T extends Button> T button(Condition findBy, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает найденную по индексу кнопку заданного типа
     * </p>
     *
     * <p>For example: {@code
     *     toolbar.button(2, StandardButton.class)
     * }</p>
     *
     * @param index порядковый номер кнопки в панил действий
     * @param componentClass требуемый возвращаемый тип
     * @return Компонент кнопка заданного типа для автотестирования
     */
    <T extends Button> T button(int index, Class<T> componentClass);
}

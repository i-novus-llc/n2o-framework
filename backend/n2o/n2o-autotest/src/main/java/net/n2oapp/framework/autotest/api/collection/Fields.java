package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.field.Field;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.snippet.Snippet;

/**
 * Поля формы для автотестирования
 */
public interface Fields extends ComponentsCollection {
    /**
     * <p>
     *     Возвращает поле страндартного по метке типа из списка полей на форме
     * </p>
     *
     * <p>For example: {@code
     *    fields().field("Поле ввода");
     * }</p>
     *
     * @param label значение метки у поля
     * @return Стандартное поле формы для автотестирования
     */
    StandardField field(String label);

    /**
     * <p>
     *     Возвращает поле страндартного по условию типа из списка полей на форме
     * </p>
     *
     * <p>For example: {@code
     *    fields().field(Condition.id("name"));
     * }</p>
     *
     * @param findBy условие поиска
     * @return Стандартное поле формы для автотестирования
     */
    StandardField field(Condition findBy);

    /**
     * <p>
     *     Возвращает первое поле типа, наследуемого от Snippet, из списка полей
     * </p>
     *
     * <p>For example: {@code
     *    fields().field(Alert.class);
     * }</p>
     *
     * @param componentClass возвращаемый тип элемента
     * @return Компонент поле для автотестирования
     */
    <T extends Snippet> T field(Class<T> componentClass);

    /**
     * <p>
     *     Возвращает поле типа, наследуемого от Field, по метке из списка полей
     * </p>
     *
     * <p>For example: {@code
     *    fields().field("Field", StandardButton.class);
     * }</p>
     *
     * @param label значение метки у поля
     * @param componentClass возвращаемый тип элемента
     * @return Компонент поле для автотестирования
     */
    <T extends Field> T field(String label, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает поле типа, наследуемого от Field, по условию из списка полей
     * </p>
     *
     * <p>For example: {@code
     *      fields().field(Condition.id("name"), StandardButton.class);
     * }</p>
     * @param findBy условие поиска
     * @param componentClass возвращаемый тип элемента
     * @return Компонент поле для автотестирования
     */
    <T extends Field> T field(Condition findBy, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает поле типа, наследуемого от Snippet, по индексу из списка полей для автотестирования
     * </p>
     *
     * <p>For example: {@code
     *      fields().field("name", Alert.class);
     * }</p>
     *
     * @param index номер поля в списке полей
     * @param componentClass возвращаемый тип элемента
     * @return Компонент поле для автотестирования
     */
    <T extends Snippet> T field(int index, Class<T> componentClass);
}

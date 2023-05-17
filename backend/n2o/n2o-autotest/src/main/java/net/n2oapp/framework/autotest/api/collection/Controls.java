package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Компоненты ввода для автотестирования
 */
//ToDo: используется ли
public interface Controls extends ComponentsCollection {

    /**
     * <p>
     *     Возвращает первый по списку компонент ввода типа, наследуемого от Control
     * </p>
     *
     * <p>For example: {@code
     *    field("Введите имя").control(InputText.class);
     * }</p>
     *
     * @param componentClass возвращаемый тип
     * @return Компонент ввода для автотестирования
     */
    <T extends Control> T control(Class<T> componentClass);

    /**
     * <p>
     *     Возвращает по индексу компонент ввода типа, наследуемого от Control
     * </p>
     *
     * <p>For example: {@code
     *    field("Введите имя").control(3, InputText.class);
     * }</p>
     *
     * @param componentClass возвращаемый тип элемента
     * @param index номер требуемого компонента ввода
     * @return Компонент ввода для автотестирования
     */
    <T extends Control> T control(int index, Class<T> componentClass);
}

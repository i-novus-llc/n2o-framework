package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;

/**
 * Меню для автотестирования
 */
public interface Menu extends ComponentsCollection {
    /**
     * <p>
     *     Возвращает кнопку с ссылкой по индексу из списка кнопок в меню
     * </p>
     *
     * <p>For example: {@code
     *    nav().anchor(0);
     * }</p>
     *
     * @param index номер кнопки из списка кнопок в меню
     * @return Компонент кнопка с ссылкой для автотестирования
     */
    AnchorMenuItem anchor(int index);

    /**
     * <p>
     *     Возвращает кнопку с ссылкой по условию из списка таких кнопок в меню
     * </p>
     *
     * <p>For example: {@code
     *    nav().anchor(Condition.visible);
     * }</p>
     *
     * @param findBy условие поиска
     * @return Компонент кнопка с ссылкой для автотестирования
     */
    AnchorMenuItem anchor(Condition findBy);

    /**
     * <p>
     *     Возвращает кнопку с выпадающим списком из меню по индексу
     * </p>
     *
     * <p>For example: {@code
     *    nav().dropdown(0);
     * }</p>
     *
     * @param index номер кнопки из списка кнопок в меню
     * @return Кнопка с выпадающим списком для автотестирования
     */
    DropdownMenuItem dropdown(int index);

    /**
     * <p>
     *     Возвращает кнопку с выпадающим списком из меню по индексу
     * </p>
     *
     * <p>For example: {@code
     *    nav().dropdown(Condition.visible);
     * }</p>
     *
     * @param findBy условие поиска
     * @return Кнопка с выпадающим списком для автотестирования
     */
    DropdownMenuItem dropdown(Condition findBy);

    /**
     * <p>
     *     Возвращает кнопку из меню по индексу
     * </p>
     *
     * <p>For example: {@code
     *    extra().item(0, AnchorMenuItem.class);
     * }</p>
     *
     * @param index номер кнопки из списка кнопок в меню
     * @param componentClass тип возвращаемой кнопки
     * @return Кнопка из меню для автотестирования
     */
    <T extends MenuItem> T item(int index, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает кнопку из меню по индексу
     * </p>
     *
     * <p>For example: {@code
     *    extra().item(Condition.visible, AnchorMenuItem.class);
     * }</p>
     *
     * @param findBy условие поиска
     * @param componentClass тип возвращаемой кнопки
     * @return Кнопка из меню для автотестирования
     */
    <T extends MenuItem> T item(Condition findBy, Class<T> componentClass);
}

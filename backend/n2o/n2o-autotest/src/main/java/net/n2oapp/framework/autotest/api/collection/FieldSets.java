package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.fieldset.FieldSet;

/**
 * Филдсеты для автотестирования
 */
public interface FieldSets extends ComponentsCollection {
    /**
     * <p>
     *     Возвращает компонент филдсет по индексу
     * </p>
     *
     * <p>For example: {@code
     *     fieldsets().fieldset(2);
     * }</p>
     *
     * @param index номер филдсета в списке филдсетов
     * @return Компонент филдсет для автотестирования
     */
    FieldSet fieldset(int index);

    /**
     * <p>
     *     Возвращает первый филдсет типа, наследуемого FieldSet
     * </p>
     *
     * <p>For example: {@code
     *     fieldsets().fieldset(MultiFieldSet.class);
     * }</p>
     *
     * @param componentClass возвращаемый тип элемента
     * @return компонент филдсет для автотестирования
     */
    <T extends FieldSet> T fieldset(Class<T> componentClass);

    /**
     * <p>
     *     Возвращает филдсет типа, наследуемого FieldSet, по индексу
     * </p>
     *
     * <p>For example: {@code
     *     fieldsets().fieldset(2, MultiFieldSet.class);
     * }</p>
     *
     * @param componentClass возвращаемый тип элемента
     * @param index номер филдсета в списке филдсетов
     * @return компонент филдсет для автотестирования
     */
    <T extends FieldSet> T fieldset(int index, Class<T> componentClass);
}

package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Выпадающий список для автотестирования
 */
public interface DropDown extends Component {

    /**
     * Возвращает элемент из выпадающего списка по номеру
     * @param index номер элемента
     * @return Элемент выпадающего списка для автотестирования
     */
    DropDownItem item(int index);

    void shouldHaveOptions(String... options);

    void selectItem(int index);

    void selectItemBy(Condition by);

    void selectMulti(int... indexes);

    void shouldBeChecked(int... indexes);

    void shouldNotBeChecked(int... indexes);

    void optionShouldHaveDescription(String option, String description);

    void optionShouldBeEnabled(String option);

    void optionShouldBeDisabled(String option);

    void optionShouldHaveStatusColor(String option, Colors color);

    /**
     * Проверка количества элементов в выпадающем списке
     * @param size ожидаемое количество
     */
    void shouldHaveOptions(int size);

    /**
     * Элемент выпадающего списка для автотестирования
     */
    interface DropDownItem extends Component, Badge {
        void shouldHaveValue(String value);
    }
}

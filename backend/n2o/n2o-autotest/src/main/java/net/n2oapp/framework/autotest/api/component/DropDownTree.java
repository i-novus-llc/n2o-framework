package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;

import java.time.Duration;

/**
 * Выпадающий список в виде древа для автотестирования
 */
public interface DropDownTree extends Component {

    /**
     * Возвращает элемент дерева по номеру
     * @param index номер элемента
     * @return Элемент дерева выпадющего списка
     */
    DropDownTreeItem item(int index);

    /**
     * Проверка количества элементов в выпадающем списке
     * @param size ожидаемое количество
     */
    void shouldHaveItems(int size);

    /**
     * Клик на поле поиска элементов
     */
    void clickOnSearchField();

    /**
     * Ввод значение в поле поиска элементов
     * @param value вводимое значение
     */
    void setValue(String value);

    /**
     * Проверка наличия внутреннего элемента по метке
     * @param label метка для поиска
     */
    void shouldHaveOption(String label);


    /**
     * Очистка поля поиска
     */
    void clear();

    interface DropDownTreeItem extends Expandable, Component, DropDown.DropDownItem {

        @Override
        default void shouldHaveDescription(String description, Duration... duration) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void shouldHaveStatusColor(Colors color) {
            throw new UnsupportedOperationException();
        }

        @Override
        default void shouldBeEnabled() {
            element().shouldNotHave(Condition.cssClass("n2o-select-tree-tree-treenode-disabled"));
        }

        @Override
        default void shouldBeDisabled() {
            element().shouldHave(Condition.cssClass("n2o-select-tree-tree-treenode-disabled"));
        }

        void click();
    }
}

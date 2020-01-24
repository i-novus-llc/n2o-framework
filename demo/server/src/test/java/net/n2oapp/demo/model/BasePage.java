package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Базовые методы для автотестов
 */

public interface BasePage {

    /**
     * Получение Страницы
     */
    static SelenideElement getPage() {
        return $(".n2o-page-body");
    }

    /**
     * Получение модального окна
     */
    static SelenideElement getModalPage() {
        return $(".modal-content");
    }

    /**
     * Получение Checkbox
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getCheckbox(SelenideElement parent, String label) {
        return parent.$$(".n2o-checkbox").findBy(Condition.text(label));
    }

    /**
     * Получение RadioButton
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getRadioButton(SelenideElement parent, String label) {
        return parent.$$(".custom-radio").findBy(Condition.text(label));
    }

    /**
     * Получение Button
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getButton(SelenideElement parent, String label) {
        return parent.$$(".btn").findBy(Condition.text(label));
    }

    /**
     * Получение input
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getInput(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-input");
    }

    /**
     * Получение input-select
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getInputSelect(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-input-select");
    }

    /**
     * Получение masked input
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getMaskedInput(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-input-mask");
    }

    /**
     * Получение input
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getInputDate(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-date-input input");
    }

    /**
     * Получение radio группы
     *
     * @param parent - начальный элемент
     * @param label  - текст
     * @return
     */
    static SelenideElement getRadioGroup(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-radio-container");
    }

    /**
     * Получени dateInput
     *
     * @param parent - ячейка таблицы
     */
    static SelenideElement getDatePicker(SelenideElement parent) {
        return parent.$(".n2o-date-picker .n2o-advanced-table-edit-control");
    }

    /**
     * Возвращает колонку таблицы
     *
     * @param parent таблица
     * @param col    - номер колонки номерация с 1
     */
    static ElementsCollection getColElements(SelenideElement parent, int col) {
        return parent.$$(".n2o-table-row td:nth-child(" + col + ")");
    }

    static ElementsCollection getRowElements(SelenideElement parent, int row) {
        return parent.$$(".n2o-table-row").get(row).$$(".n2o-advanced-table-cell-expand");
    }

    /**
     * Полчение строки в виджете list
     * @param parent    виджет
     * @param row   номер строки
     * @return
     */
    static SelenideElement getListItem(SelenideElement parent, int row) {
        return parent.$$(".n2o-widget-list-item").get(row);
    }

    /**
     * Получение главного контейнера в строке виджет list
     * @param parent  строка в виджете list
     * @return
     */
    static ElementsCollection getListItemMainContainer(SelenideElement parent) {
        return parent.$$(".n2o-widget-list-item-main-container");
    }

    /**
     * Полчение списка строк в виджете list
     * @param parent    виджет
     * @return
     */
    static ElementsCollection getListItems(SelenideElement parent) {
        return parent.$$(".n2o-widget-list-item");
    }

    /**
     * Получение колонки таблицы (по возможности использовать getColElements)
     *
     * @param rows - строки таблицы
     * @param col  - номер колонки
     */
    static List<String> getCol(ElementsCollection rows, int col) {
        List<String> result = new ArrayList<>();
        rows.forEach(row -> result.add(getCellValue(row.$$("td").get(col))));
        return result;
    }

    /**
     * Получение строки таблицы (по возможности использовать getRowElements)
     *
     * @param rows строки таблицы
     * @param row  - номер строки
     */
    static List<String> getRow(ElementsCollection rows, int row) {
        List<String> result = new ArrayList<>();
        rows.get(row).$$("td").forEach(col -> result.add(getCellValue(col)));
        return result;
    }

    /**
     * Извлечение значения из ячейки таблицы
     */
    static String getCellValue(SelenideElement cell) {
        return cell.$("input[type=\"checkbox\"]").is(Condition.exist) ? cell.$("input").getAttribute("checked") : cell.getText();
    }

}

package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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
     * Получение регионов
     */
    static ElementsCollection getRegions() {
        return $$(".n2o-panel-region");
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
     * Получение поля Checkbox
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getCheckboxInput(SelenideElement parent, String label) {
        return getCheckbox(parent, label).$(".n2o-input");
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
     * Получение input
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getInputDate(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-date-input input");
    }

    /**
     * Получени dateInput
     *
     * @param parent - ячейка таблицы
     */
    static SelenideElement getDatePicker(SelenideElement parent) {
        return parent.$(".n2o-date-picker .n2o-advanced-table-edit-control");
    }

    static SelenideElement getModalDialog(String title) {
        return $$(".modal-dialog").findBy(Condition.text(title));
    }

    static ElementsCollection getAlerts() {
        return $$(".n2o-alerts .n2o-alert-body").filter(Condition.visible);
    }

    static SelenideElement getModalDialogBody(String title) {
        return getModalDialog(title).$(".modal-body");
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
     * Скролирование страницы
     *
     * @param x pix
     * @param y pix
     */
    static void scrollPage(int x, int y) {
        Selenide.executeJavaScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
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

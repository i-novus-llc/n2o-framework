package net.n2oapp.demo;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовые методы для автотестов
 */

public interface BasePage {

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
     * @return
     */
    static SelenideElement getInput(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-input");
    }

    /**
     * Получение стартового значения интервала даты
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getDateIntervalStart(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-date-input-first input");
    }

    /**
     * Получение конечного значения интервала даты
     *
     * @param parent - начальный элемент
     * @param label  - текст
     */
    static SelenideElement getDateIntervalEnd(SelenideElement parent, String label) {
        return parent.$$(".n2o-form-group").findBy(Condition.text(label)).$(".n2o-date-input-last input");
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

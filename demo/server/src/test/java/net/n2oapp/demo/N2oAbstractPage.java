package net.n2oapp.demo;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовые методы для автотестов
 */

public interface N2oAbstractPage {

    /**
     * Получение колонки таблицы
     *
     * @param rows - строки таблицы
     * @param col - номер колонки
     */
    default List<String> getCol(ElementsCollection rows, int col) {
        List<String> result = new ArrayList<>();
        rows.forEach(row -> result.add(getCellValue(row.$$("td").get(col))));
        return result;
    }

    /**
     * Получение строки таблицы
     *
     * @param rows строки таблицы
     * @param row - номер строки
     */
    default List<String> getRow(ElementsCollection rows, int row) {
        List<String> result = new ArrayList<>();
        rows.get(row).$$("td").forEach(col -> result.add(getCellValue(col)));
        return result;
    }

    /**
     * Извлечение значения из ячейки таблицы
     */
    default String getCellValue(SelenideElement cell) {
        return cell.$("input[type=\"checkbox\"]").is(Condition.exist) ? cell.$("input").getAttribute("checked") : cell.getText();
    }

    //

    default boolean isSorted(List<String> list, Boolean dir) {
        String p = null;
        for (String e : list) {
            if (p == null) {
                p = e;
                continue;
            }
            if (dir && p.compareTo(e) >= 0 || !dir && p.compareTo(e) <= 0) return false;
        }
        return true;
    }

    default <T> boolean isAllMatch(List<T> list, T value) {
        return list.stream().allMatch(value::equals);
    }
}

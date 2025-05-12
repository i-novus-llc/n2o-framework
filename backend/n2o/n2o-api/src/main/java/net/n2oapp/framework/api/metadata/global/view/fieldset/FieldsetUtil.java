package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.control.N2oField;

import java.util.List;

/**
 * Утилитный класс, формирующий список всех fields у fieldset
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldsetUtil {

    /**
     * Формирует лист всех fieldов fieldseta
     *
     * @param fieldSet       исходный fieldset
     * @param fieldArrayList промежутточный лист fieldов
     * @return ArrayList<N2oField> лист всех fieldов fieldseta
     */

    public static List<N2oField> getAllFields(N2oFieldSet fieldSet, List<N2oField> fieldArrayList) {
        for (FieldsetItem fieldsetItem : fieldSet.getItems()) {
            if (fieldsetItem instanceof N2oField item) {
                fieldArrayList.add(item);
            }
            if (fieldsetItem instanceof N2oFieldsetRow item) {
                fieldArrayList = getAllFieldsInRow(item, fieldArrayList);
            }
            if (fieldsetItem instanceof N2oFieldsetCol item) {
                fieldArrayList = getAllFieldsInCol(item, fieldArrayList);

            }
            if (fieldsetItem instanceof N2oFieldSet item) {
                fieldArrayList = getAllFields(item, fieldArrayList);
            }

        }
        return fieldArrayList;
    }

    /**
     * Формирует лист всех fieldов строки
     *
     * @param row            строка исходного fieldSetа
     * @param fieldArrayList промежутточный лист fieldов
     * @return ArrayList<N2oField> лист всех fieldов fieldseta
     */

    private static List<N2oField> getAllFieldsInRow(N2oFieldsetRow row, List<N2oField> fieldArrayList) {
        for (FieldsetItem fieldsetItem : row.getItems()) {
            if (fieldsetItem instanceof N2oField item) {
                fieldArrayList.add(item);
            }
            if (fieldsetItem instanceof N2oFieldsetCol item) {
                fieldArrayList = getAllFieldsInCol(item, fieldArrayList);
            }
            if (fieldsetItem instanceof N2oFieldSet item) {
                fieldArrayList = getAllFields(item, fieldArrayList);
            }
        }
        return fieldArrayList;
    }

    /**
     * Формирует лист всех field-ов колонки
     *
     * @param col            колонка исходного fieldSet-а
     * @param fieldArrayList промежуточный лист field-ов
     * @return ArrayList<N2oField> лист всех field-ов fieldset-a
     */

    private static List<N2oField> getAllFieldsInCol(N2oFieldsetCol col, List<N2oField> fieldArrayList) {
        for (FieldsetItem fieldsetItem : col.getItems()) {
            if (fieldsetItem instanceof N2oField item) {
                fieldArrayList.add(item);
            }
            if (fieldsetItem instanceof N2oFieldSet item) {
                fieldArrayList = getAllFields(item, fieldArrayList);
            }
        }
        return fieldArrayList;
    }

}

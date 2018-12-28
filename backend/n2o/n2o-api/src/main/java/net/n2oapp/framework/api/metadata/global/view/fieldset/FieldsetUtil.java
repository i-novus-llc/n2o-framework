package net.n2oapp.framework.api.metadata.global.view.fieldset;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.N2oField;

import java.util.ArrayList;

/**
 * Утилитный класс, формирующий список всех fields у fieldset
 */
public class FieldsetUtil {

    /**
     * Формирует лист всех fieldов fieldseta
     *
     * @param fieldSet       исходный fieldset
     * @param fieldArrayList промежутточный лист fieldов
     * @return ArrayList<N2oField> лист всех fieldов fieldseta
     */

    public static ArrayList<N2oField> getAllFields(N2oFieldSet fieldSet, ArrayList<N2oField> fieldArrayList) {
        for (NamespaceUriAware item : fieldSet.getItems()) {
            if (item instanceof N2oField) {
                fieldArrayList.add((N2oField) item);
            }
            if (item instanceof N2oFieldsetRow) {
                fieldArrayList = getAllFieldsInRow((N2oFieldsetRow) item,  fieldArrayList);
            }
            if (item instanceof N2oFieldsetColumn) {
                fieldArrayList = getAllFieldsInCol((N2oFieldsetColumn) item, fieldArrayList);

            }
            if (item instanceof N2oFieldSet) {
                fieldArrayList = getAllFields((N2oFieldSet) item, fieldArrayList);
            }

        }
        return fieldArrayList;
    }

    /**
     * Формирует лист всех fieldов строки
     *
     * @param row строка исходного fieldSetа
     * @param fieldArrayList промежутточный лист fieldов
     * @return ArrayList<N2oField> лист всех fieldов fieldseta
     */

    private static ArrayList<N2oField> getAllFieldsInRow(N2oFieldsetRow row, ArrayList<N2oField> fieldArrayList) {
        for (NamespaceUriAware item : row.getItems()) {
            if (item instanceof N2oField) {
                fieldArrayList.add((N2oField) item);
            }
            if (item instanceof N2oFieldsetColumn) {
                fieldArrayList = getAllFieldsInCol((N2oFieldsetColumn) item, fieldArrayList);
            }
            if (item instanceof N2oFieldSet) {
                fieldArrayList = getAllFields((N2oFieldSet) item, fieldArrayList);
            }
        }
        return fieldArrayList;
    }

    /**
     * Формирует лист всех fieldов колонки
     *
     * @param col колонка исходного fieldSetа
     * @param fieldArrayList промежутточный лист fieldов
     * @return ArrayList<N2oField> лист всех fieldов fieldseta
     */

    private static ArrayList<N2oField> getAllFieldsInCol(N2oFieldsetColumn col, ArrayList<N2oField> fieldArrayList) {
        for (NamespaceUriAware item : col.getItems()) {
            if (item instanceof N2oField) {
                fieldArrayList.add((N2oField) item);
            }
            if (item instanceof N2oFieldSet) {
                fieldArrayList = getAllFields((N2oFieldSet) item,  fieldArrayList);
            }
        }
        return fieldArrayList;
    }

}

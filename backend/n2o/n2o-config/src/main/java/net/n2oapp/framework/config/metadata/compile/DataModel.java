package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.util.SubModelsProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Модель данных
 */
public class DataModel {
    /**
     * Хранение моделей данных виджетов
     */
    private Map<ModelLink, DataSet> store;

    public DataModel() {
        this.store = new HashMap<>();
    }

    /**
     * Добавить все ссылки на данные в модель данных
     * @param links Ссылки привязанные к ключам
     * @param data Данные привязанные к ключам
     */
    public void addAll(Map<String, ModelLink> links, DataSet data) {
        if (links == null)
            return;
        for (Map.Entry<String, ModelLink> entry : links.entrySet()) {
            add(entry.getValue(), data.get(entry.getKey()));
        }
    }

    /**
     * Добавить ссылку на данные в модель данных
     * @param link Ссылка
     * @param value Значение
     * @return Предыдущее значение по ссылке
     */
    public Object add(ModelLink link, Object value) {
        if (link.isConst())
            return null;
        ModelLink widgetLink = getAndCheckWidgetLink(link);
        String fieldId = link.getFieldId();
        if (fieldId != null) {
            DataSet data = store.get(widgetLink);
            if (data == null)
                data = new DataSet();
            Object old = data.put(fieldId, value);
            store.put(widgetLink, data);
            return old;
        } else {
            if (value != null && !(link.getValue() instanceof DataSet))
                throw new IllegalArgumentException("Value " + value + " is not a DataSet");
            return store.put(widgetLink, (DataSet) value);
        }
    }

    /**
     * Получить значение поля по ссылке на поле
     * @param link Ссылка
     * @return Значение поля
     */
    public Object getValue(ModelLink link) {
        DataSet data = store.get(getAndCheckWidgetLink(link));
        if (data == null)
            return null;
        String fieldId = link.getFieldId();
        return data.get(fieldId);
    }

    /**
     * Получить значение поля по ссылке на модель и полю
     * @param link Ссылка на модель
     * @param field Поле
     * @return Значение поля
     */
    public Object getValue(ModelLink link, String field) {
        DataSet data = getData(link);
        if (data == null)
            return null;
        return data.get(field);
    }

    /**
     * Получить данные модели по ссылке на модель
     * @param link Ссылка на модель
     * @return Данные модели
     */
    public DataSet getData(ModelLink link) {
        return store.get(getAndCheckWidgetLink(link));
    }

    /**
     * Получить функцию данных модели по ссылке на модель и процессору вложенных моделей.
     * В случае отсутствия данных в модели запускается попытка получения вложенных моделей из процессора
     * @param link Ссылка на модель
     * @param processor Процессор вложенных моделей
     * @return Функция данных модели
     */
    public Function<String, Object> getDataIfAbsent(ModelLink link, SubModelsProcessor processor) {
        ModelLink widgetLink = getAndCheckWidgetLink(link);
        DataSet data = store.get(widgetLink);
        if (data == null) {
            return key -> null;
        } else if (widgetLink.getSubModelQuery() != null) {
            return key -> {
                Object value = data.get(key);
                if (value == null && widgetLink.getSubModelQuery() != null) {
                    processor.executeSubModels(Collections.singletonList(widgetLink.getSubModelQuery()), data);
                    return data.get(key);
                } else {
                    return value;
                }
            };
        } else {
            return data::get;
        }
    }

    private ModelLink getAndCheckWidgetLink(ModelLink link) {
        if (link == null)
            throw new IllegalArgumentException("Link is null");
        ModelLink widgetLink = link.getWidgetLink();
        if (widgetLink == null)
            throw new IllegalArgumentException("Link " + link + " doesn't contains model and widget");
        return widgetLink;
    }
}

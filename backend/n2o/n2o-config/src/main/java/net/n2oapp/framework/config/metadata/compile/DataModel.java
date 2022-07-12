package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.api.util.SubModelsProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
     *
     * @param links Ссылки привязанные к ключам
     * @param data  Данные привязанные к ключам
     */
    public void addAll(Map<String, ModelLink> links, DataSet data) {
        if (links == null || data == null)
            return;
        for (Map.Entry<String, ModelLink> entry : links.entrySet()) {
            add(entry.getKey(), entry.getValue(), data.get(entry.getKey()));
        }
    }

    /**
     * Добавить ссылку на данные в модель данных
     *
     *
     * @param key   Ключ, по которому хранится ссылка
     * @param link  Ссылка
     * @param value Значение
     * @return Предыдущее значение по ссылке
     */
    public Object add(String key, ModelLink link, Object value) {
        ModelLink widgetLink = link.getWidgetLink();
        String fieldId = link.getFieldId();
        if (fieldId != null) {
            if (widgetLink != null && widgetLink.getSubModelQuery() != null)
                addSubModelToStoreKey(widgetLink);
            return storeData(widgetLink, fieldId, value);
        } else {
            widgetLink = widgetLink != null ? widgetLink : new ModelLink();
            if (value != null && !(link.getValue() instanceof DataSet)) {
                widgetLink.setValue(ScriptProcessor.resolveExpression(value.toString()));
                return storeData(widgetLink, key, value);
            }
            return store.put(widgetLink, (DataSet) value);
        }
    }

    /**
     * Добавить данные в store
     *
     * @param widgetLink Ссылка на модель
     * @param key        Ключ, по которому будут храниться данные
     * @param value      Данные
     * @return Предыдущее значение по ссылке
     */
    private Object storeData(ModelLink widgetLink, String key, Object value) {
        DataSet data = store.get(widgetLink);
        if (data == null)
            data = new DataSet();
        Object old = data.put(key, value);
        store.put(widgetLink, data);
        return old;
    }

    /**
     * Добавление сабмодели в ссылку (ключ) хранилища, если она отсутствовала
     *
     * @param link Ссылка
     */
    private void addSubModelToStoreKey(ModelLink link) {
        ModelLink storeKey = store.keySet().stream().filter(k -> k.equals(link)).findFirst().orElse(null);
        if (storeKey != null && storeKey.getSubModelQuery() == null)
            storeKey.setSubModelQuery(link.getSubModelQuery());
    }

    /**
     * Получить значение поля по ссылке на поле
     *
     * @param link Ссылка
     * @return Значение поля
     */
    public Object getValue(ModelLink link) {
        if (link == null)
            return null;
        ModelLink widgetLink = link.getWidgetLink();
        if (widgetLink == null && link.isConst())
            return link.getValue();
        if (widgetLink == null)
            return null;
        DataSet data = store.get(widgetLink);
        if (data == null)
            return null;
        String fieldId = link.getFieldId();
        return data.get(fieldId);
    }

    /**
     * Получить значение поля по ссылке на модель и полю
     *
     * @param link  Ссылка на модель
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
     *
     * @param link Ссылка на модель
     * @return Данные модели
     */
    public DataSet getData(ModelLink link) {
        if (link == null)
            return null;
        ModelLink widgetLink = link.getWidgetLink();
        if (widgetLink == null)
            return null;
        return store.get(widgetLink);
    }

    /**
     * Получить функцию данных модели по ссылке на модель и процессору вложенных моделей.
     * В случае отсутствия данных в модели запускается попытка получения вложенных моделей из процессора
     *
     * @param link      Ссылка на модель
     * @param processor Процессор вложенных моделей
     * @return Функция данных модели
     */
    public Function<String, Object> getDataIfAbsent(ModelLink link, SubModelsProcessor processor) {
        Map.Entry<ModelLink, DataSet> entry = getKeyEntry(link);
        if (entry == null) {
            return key -> null;
        } else if (entry.getKey().getSubModelQuery() != null) {
            return key -> {
                Object value = entry.getValue().get(key);
                if (value == null && entry.getKey().getSubModelQuery() != null) {
                    if (processor != null)
                        processor.executeSubModels(Collections.singletonList(entry.getKey().getSubModelQuery()), entry.getValue());
                    return entry.getValue().get(key);
                } else {
                    return value;
                }
            };
        } else {
            return entry.getValue()::get;
        }
    }

    private Map.Entry<ModelLink, DataSet> getKeyEntry(ModelLink link) {
        if (link == null)
            return null;
        ModelLink widgetLink = link.getWidgetLink();
        return findEntryByLink(Objects.requireNonNullElse(widgetLink, link));
    }

    private Map.Entry<ModelLink, DataSet> findEntryByLink(ModelLink link) {
        return store.entrySet().stream().filter(e -> e.getKey().equals(link)).findFirst().orElse(null);
    }

}

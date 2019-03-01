package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.HashMap;
import java.util.Map;

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

    public void addAll(Map<String, ModelLink> links, DataSet data) {
        if (links == null)
            return;
        for (Map.Entry<String, ModelLink> entry : links.entrySet()) {
            add(entry.getValue(), data.get(entry.getKey()));
        }
    }

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

    public Object getValue(ModelLink link) {
        DataSet data = store.get(getAndCheckWidgetLink(link));
        if (data == null)
            return null;
        String fieldId = link.getFieldId();
        return data.get(fieldId);
    }

    public Object getValue(ModelLink link, String field) {
        DataSet data = getData(link);
        if (data == null)
            return null;
        return data.get(field);
    }

    public DataSet getData(ModelLink link) {
        return store.get(getAndCheckWidgetLink(link));
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

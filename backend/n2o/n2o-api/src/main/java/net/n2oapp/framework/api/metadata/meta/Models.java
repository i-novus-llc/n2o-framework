package net.n2oapp.framework.api.metadata.meta;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

import java.util.HashMap;
import java.util.Objects;

/**
 * Клиентская модель списка моделей данных
 */
public class Models extends HashMap<String, ModelLink> implements Compiled {

    public void add(ReduxModelEnum model, String widgetId, String field, ModelLink value) {
        add(model, widgetId, null, field, value);
    }

    public void add(ReduxModelEnum model, String widgetId, String suffix, String field, ModelLink value) {
        if (field != null)
            put(String.format("%s['%s']%s.%s", model.getId(), widgetId, Objects.toString(suffix, ""), field), new ModelLink(value));
        else add(model, widgetId, value);
    }

    public void add(ReduxModelEnum model, String widgetId, ModelLink value) {
        put(String.format("%s['%s']", model.getId(), widgetId), new ModelLink(value));
    }

    public void add(ModelLink link, ModelLink value) {
        add(link.getModel(), link.getDatasource(), link.getSuffix(), link.getFieldId(), new ModelLink(value));
    }

    public void add(ModelLink link, Object value) {
        add(link.getModel(), link.getDatasource(), link.getSuffix(), link.getFieldId(), new ModelLink(value));
    }

    public ModelLink get(ReduxModelEnum model, String widgetId, String field) {
        return get(model, widgetId, null, field);
    }

    public ModelLink get(ReduxModelEnum model, String widgetId, String suffix, String field) {
        return get(String.format("%s['%s']%s.%s", model.getId(), widgetId, Objects.toString(suffix, ""), field));
    }

    public ModelLink get(ModelLink link) {
        return get(link.getModel(), link.getDatasource(), link.getSuffix(), link.getFieldId());
    }

}

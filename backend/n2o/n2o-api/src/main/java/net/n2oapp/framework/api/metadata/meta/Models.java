package net.n2oapp.framework.api.metadata.meta;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.util.HashMap;

/**
 * Клиентская модель списка моделей данных
 */
public class Models extends HashMap<String, ModelLink> implements Compiled {

    public void add(ReduxModel model, String widgetId, String field, ModelLink value) {
        if (field != null)
            put(String.format("%s['%s'].%s", model.getId(), widgetId, field), new ModelLink(value));
        else add(model, widgetId, value);
    }

    public void add(ReduxModel model, String widgetId, ModelLink value) {
        put(String.format("%s['%s']", model.getId(), widgetId), new ModelLink(value));
    }

    public void add(ModelLink link, ModelLink value) {
        add(link.getModel(), link.getWidgetId(), link.getFieldId(), new ModelLink(value));
    }

    public void add(ModelLink link, Object value) {
        add(link.getModel(), link.getWidgetId(), link.getFieldId(), new ModelLink(value));
    }

    public ModelLink get(ReduxModel model, String widgetId, String field) {
        return get(String.format("%s['%s'].%s", model.getId(), widgetId, field));
    }

    public ModelLink get(ModelLink link) {
        return get(link.getModel(), link.getWidgetId(), link.getFieldId());
    }

}

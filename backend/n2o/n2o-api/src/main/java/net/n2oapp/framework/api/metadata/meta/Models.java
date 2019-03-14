package net.n2oapp.framework.api.metadata.meta;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.util.HashMap;

/**
 * Клиентская модель списка моделей данных
 */
public class Models extends HashMap<String, ModelLink> implements Compiled {

    public void add(ReduxModel model, String widgetId, String field, ModelLink link) {
        put(String.format("%s['%s'].%s", model.getId(), widgetId, field), link);
    }

    public void add(ReduxModel model, String widgetId, ModelLink link) {
        put(String.format("%s['%s']", model.getId(), widgetId), link);
    }

    public ModelLink get(ReduxModel model, String widgetId, String field) {
        return get(String.format("%s['%s'].%s", model.getId(), widgetId, field));
    }

}

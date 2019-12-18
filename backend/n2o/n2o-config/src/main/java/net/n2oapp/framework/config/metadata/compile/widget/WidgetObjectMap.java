package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Соответсвие идентификатора виджета и объекта
 */
public class WidgetObjectMap extends HashMap<String, CompiledObject> implements Serializable {
    @Override
    public CompiledObject put(String widgetId, CompiledObject object) {
        return super.put(widgetId, object);
    }

    public CompiledObject getObject(String widgetId) {
        return super.get(widgetId);
    }
}

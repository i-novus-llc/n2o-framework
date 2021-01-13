package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Соответствие идентификатора виджета и объекта
 * Ключ - идентификатор виджета
 * Значение - объект
 */
public class WidgetObjectScope extends HashMap<String, CompiledObject> implements Serializable {

    public CompiledObject getObject(String widgetId) {
        return super.get(widgetId);
    }
}

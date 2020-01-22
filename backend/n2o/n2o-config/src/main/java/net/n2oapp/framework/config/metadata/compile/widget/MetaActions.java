package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.HashMap;

/**
 * Карта собранных действий страницы или виджета
 */
public class MetaActions extends HashMap<String, Action> {

    public void addAction(String id, Action action) {
        put(id, action);
    }
}

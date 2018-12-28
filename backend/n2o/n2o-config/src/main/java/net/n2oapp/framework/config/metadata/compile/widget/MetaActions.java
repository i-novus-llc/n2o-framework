package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.action.Action;

/**
 * Карта собранных действий страницы или виджета
 */
public class MetaActions extends StrictMap<String, Action> {

    public void addAction(Action action) {
        put(action.getId(), action);
    }
}

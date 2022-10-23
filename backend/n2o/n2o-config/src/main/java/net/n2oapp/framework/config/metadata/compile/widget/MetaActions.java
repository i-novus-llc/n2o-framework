package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;

/**
 * Карта собранных действий страницы или виджета
 */
public class MetaActions extends StrictMap<String, ActionBar> {

    public void addAction(String id, ActionBar action) {
        put(id, action);
    }
}

package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.ActionBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Карта собранных действий страницы или виджета
 */
public class MetaActions extends HashMap<String, ActionBar> {

    public MetaActions() {
    }

    public MetaActions(Map<String, ? extends ActionBar> m) {
        super(m);
    }

    public void addAction(String id, ActionBar action) {
        put(id, action);
    }
}

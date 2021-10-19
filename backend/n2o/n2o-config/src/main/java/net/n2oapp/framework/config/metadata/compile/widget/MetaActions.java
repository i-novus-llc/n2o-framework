package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.ActionsBar;

import java.util.HashMap;

/**
 * Карта собранных действий страницы или виджета
 */
public class MetaActions extends HashMap<String, ActionsBar> {

    public void addAction(String id, ActionsBar action) {
        put(id, action);
    }
}

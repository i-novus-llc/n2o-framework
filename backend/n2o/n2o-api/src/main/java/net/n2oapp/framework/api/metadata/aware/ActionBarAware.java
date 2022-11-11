package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;

/**
 * Знание о метадействиях компонента
 */
public interface ActionBarAware extends Source {
    ActionBar[] getActions();
}

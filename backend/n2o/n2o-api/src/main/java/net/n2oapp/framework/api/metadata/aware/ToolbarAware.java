package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * Знание о тулбарах компонента
 */
public interface ToolbarAware extends Source {
    N2oToolbar[] getToolbars();
}

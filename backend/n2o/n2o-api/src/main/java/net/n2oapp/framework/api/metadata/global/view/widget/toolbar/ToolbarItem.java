package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.io.Serializable;
import java.util.List;

/**
 * Маркер для item в toolbar
 */
public interface ToolbarItem extends Serializable {

    List<N2oAction> getActions();
}

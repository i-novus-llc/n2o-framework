package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

import java.util.List;

/**
 * Маркер для item в toolbar
 */
public interface ToolbarItem extends NamespaceUriAware, Source, Cloneable {

    List<N2oAction> getListActions();

    ToolbarItem clone();
}

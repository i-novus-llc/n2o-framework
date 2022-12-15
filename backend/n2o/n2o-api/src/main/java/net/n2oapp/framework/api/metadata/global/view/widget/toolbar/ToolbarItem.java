package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.action.N2oAction;

import java.util.List;

/**
 * Маркер для item в toolbar
 */
public interface ToolbarItem extends NamespaceUriAware, Source {

    List<N2oAction> getListActions();
}

package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.jackson.ComponentType;

import java.io.Serializable;
import java.util.List;

/**
 * Маркер для item в toolbar
 */
@ComponentType
public interface ToolbarItem extends Serializable, NamespaceUriAware, Source {

    List<N2oAction> getActions();
}

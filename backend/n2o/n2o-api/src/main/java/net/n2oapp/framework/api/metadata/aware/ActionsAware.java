package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAction;

/**
 * Знание об исходных действиях компонента
 */
public interface ActionsAware extends Source {
    String getActionId();
    void setActionId(String actionId);
    N2oAction[] getActions();
    void setActions(N2oAction[] actions);
}

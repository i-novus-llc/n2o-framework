package net.n2oapp.framework.api.metadata.event.system;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

/**
 * @author operehod
 * @since 22.10.2015
 */
public class SystemAnchor extends N2oAbstractAction implements N2oAction {

    private String href;

    public String getTarget() {
        return "self";
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String getSrc() {
        return "n2o/controls/action/states/anchor.state";
    }
}

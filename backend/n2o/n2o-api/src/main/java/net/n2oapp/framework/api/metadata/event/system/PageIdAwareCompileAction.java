package net.n2oapp.framework.api.metadata.event.system;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;

/**
 * @author operehod
 * @since 13.11.2015
 */
public interface PageIdAwareCompileAction extends N2oAction {

    public String getPageId();

}

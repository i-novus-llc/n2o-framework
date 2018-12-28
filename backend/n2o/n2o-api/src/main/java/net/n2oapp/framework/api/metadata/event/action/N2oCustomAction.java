package net.n2oapp.framework.api.metadata.event.action;

import net.n2oapp.framework.api.metadata.aware.PropertiesAware;

import java.util.Map;

/**
 * @author operehod
 * @since 22.10.2015
 */
public class N2oCustomAction extends N2oAbstractAction implements N2oAction, PropertiesAware {

    private Map<String, Object> properties;

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}

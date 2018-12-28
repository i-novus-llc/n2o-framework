package net.n2oapp.framework.api.metadata.event.action;

import net.n2oapp.framework.api.metadata.aware.PropertiesAware;

import java.io.Serializable;
import java.util.Map;

/**
 * Событие on-click
 */
public class OnClick extends N2oAbstractAction implements Serializable, PropertiesAware {
    private String sourceSrc;
    private String functionName;
    private Map<String, Object> properties;
    //todo:добавить src

    public String getSourceSrc() {
        return sourceSrc;
    }

    public void setSourceSrc(String sourceSrc) {
        this.sourceSrc = sourceSrc;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String getSrc() {
        return "n2o/controls/action/states/on.click.state";
    }
}

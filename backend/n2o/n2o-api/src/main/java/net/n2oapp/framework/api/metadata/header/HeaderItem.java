package net.n2oapp.framework.api.metadata.header;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;

import java.util.Map;

@Getter
@Setter
public class HeaderItem implements Compiled, PropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String label;
    @JsonProperty
    private String href;
    @JsonProperty
    private LinkType linkType;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String image;
    @JsonProperty
    private Target target;
    @JsonProperty
    private SimpleMenu subItems;
    @JsonProperty("type")
    private String type;
    private String pageId;
    private Map<String, Object> properties;

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return properties;
    }
    public enum LinkType {
        inner, outer
    }
}

package net.n2oapp.framework.api.metadata.header;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
public class HeaderItem implements Compiled, PropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String title;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String badgeColor;
    @JsonProperty
    private Object badge;
    @JsonProperty
    private String imageSrc;
    @JsonProperty
    private ImageShape imageShape;
    @JsonProperty("items")
    private ArrayList<HeaderItem> subItems;
    @JsonProperty("type")
    private String type;
    @JsonProperty
    private String href;
    @JsonProperty
    private LinkType linkType;
    @JsonProperty
    private Target target;
    @Deprecated
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

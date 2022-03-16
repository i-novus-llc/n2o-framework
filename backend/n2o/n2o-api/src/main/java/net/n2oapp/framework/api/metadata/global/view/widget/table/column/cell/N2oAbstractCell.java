package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;

import java.util.Map;

/**
 * Абстрактная ячейка
 */
@Getter
@Setter
public abstract class N2oAbstractCell implements N2oCell, ExtensionAttributesAware, CssClassAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String fieldKey;
    @JsonProperty
    private String src;
    private String namespaceUri;
    @JsonProperty("className")
    private String cssClass;
    @JsonProperty("style")
    private Map<String, String> reactStyle;
    private String style;
    private String visible;
    @JsonProperty("visible")
    private Object jsonVisible;
    @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
    @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
    private Map<N2oNamespace, Map<String, String>> extAttributes;
    private Map<String, Object> properties;
    @JsonProperty
    private String tooltipFieldId;
    @JsonProperty
    private Boolean hideOnBlur;

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return properties;
    }
}

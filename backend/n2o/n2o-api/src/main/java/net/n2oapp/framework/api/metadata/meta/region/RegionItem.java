package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RegionItem implements Compiled, JsonPropertiesAware {
    @JsonProperty
    private String id = "item";
    @JsonProperty
    private String label;
    @JsonProperty
    private List<Compiled> content;
    private Map<String, Object> properties;
}

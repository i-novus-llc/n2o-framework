package net.n2oapp.framework.api.metadata.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SimpleMenu implements Compiled, JsonPropertiesAware {
    @JsonProperty
    private String src;
    @JsonProperty
    private List<MenuItem> items;
    private Map<String, Object> properties;
}

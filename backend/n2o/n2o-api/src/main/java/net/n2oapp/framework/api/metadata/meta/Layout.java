package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.Region;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель layout
 */
@Getter
@Setter
public class Layout implements Compiled {
    @JsonProperty
    private String src;
    @JsonProperty
    private String label;
    @JsonProperty
    private Map<String, List<Region>> regions;
}

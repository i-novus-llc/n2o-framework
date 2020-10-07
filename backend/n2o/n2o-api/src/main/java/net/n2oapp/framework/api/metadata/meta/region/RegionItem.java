package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.List;

@Getter
@Setter
public class RegionItem implements Compiled {
    @JsonProperty
    private String id = "item";
    @JsonProperty
    private String label;
    @JsonProperty
    private List<Compiled> content;
}

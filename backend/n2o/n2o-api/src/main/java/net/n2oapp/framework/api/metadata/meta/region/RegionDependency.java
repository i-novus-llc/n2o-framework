package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;

import java.util.List;

@Getter
@Setter
public class RegionDependency implements Compiled {
    @JsonProperty
    private List<DependencyCondition> visible;
}

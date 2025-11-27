package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель региона {@code <col>}
 */
@Getter
@Setter
public class ColRegion extends Region implements CompiledRegionItem {
    @JsonProperty
    private Integer size;
    @JsonProperty
    private Integer offset;
}
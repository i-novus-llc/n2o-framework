package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.region.AlignEnum;
import net.n2oapp.framework.api.metadata.global.view.region.JustifyEnum;

/**
 * Клиентская модель региона {@code <flex-row>}
 */
@Getter
@Setter
public class FlexRowRegion extends Region implements CompiledRegionItem {
    @JsonProperty
    private Boolean wrap;
    @JsonProperty
    private AlignEnum align;
    @JsonProperty
    private JustifyEnum justify;
}
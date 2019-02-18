package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель региона с горизонтальным делителем.
 */
@Getter
@Setter
public class LineRegion extends Region {
    @JsonProperty
    private Boolean collapsible;

    @Override
    @JsonProperty
    public List<? extends Item> getItems() {
        return super.getItems();
    }
}

package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель простого региона
 * @Deprecated replaced by {@link CustomRegion}
 */
@Deprecated
@Getter
@Setter
public class NoneRegion extends Region {

    @Override
    @JsonProperty
    public List<? extends Item> getItems() {
        return super.getItems();
    }

}

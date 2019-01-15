package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель кастомного региона
 */
@Getter
@Setter
public class CustomRegion extends Region {

    @Override
    @JsonProperty
    public List<? extends Item> getItems() {
        return super.getItems();
    }
}

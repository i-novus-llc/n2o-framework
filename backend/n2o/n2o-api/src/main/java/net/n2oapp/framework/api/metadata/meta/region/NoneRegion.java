package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Клиентская модель простого региона
 */
@Getter
@Setter
public class NoneRegion extends Region {

    @Override
    @JsonProperty
    public List<? extends Item> getItems() {
        return super.getItems();
    }

}

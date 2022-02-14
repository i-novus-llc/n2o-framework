package net.n2oapp.framework.api.metadata.meta.region.scrollspy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.region.Region;

import java.util.List;

/**
 * Клиентская модель региона с отслеживанием прокрутки
 */
@Getter
@Setter
public class ScrollspyRegion extends Region {

    @JsonProperty
    private String placement;
    @JsonProperty
    private String title;
    @JsonProperty
    private String active;
    @JsonProperty
    private Boolean headlines;
    @JsonProperty
    private List<ScrollspyElement> menu;
}

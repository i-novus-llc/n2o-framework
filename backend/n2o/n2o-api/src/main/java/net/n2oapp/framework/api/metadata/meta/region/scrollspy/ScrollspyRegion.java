package net.n2oapp.framework.api.metadata.meta.region.scrollspy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;

/**
 * Клиентская модель региона с отслеживанием прокрутки
 */
@Getter
@Setter
public class ScrollspyRegion extends Region implements CompiledRegionItem {

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

    @Override
    public void collectWidgets(List<Widget<?>> compiledWidgets) {
        collectWidgets(menu, compiledWidgets);
    }
}

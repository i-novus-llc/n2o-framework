package net.n2oapp.framework.api.metadata.meta.region.scrollspy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;

/**
 * Клиентская модель элемента scrollspy-региона с вложенными элементами
 */
@Getter
@Setter
public class GroupScrollspyElement extends ScrollspyElement implements CompiledRegionItem {

    @JsonProperty
    private List<ScrollspyElement> menu;

    @Override
    public void collectWidgets(List<Widget<?>> compiledWidgets) {
        collectWidgets(menu, compiledWidgets);
    }
}

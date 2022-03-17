package net.n2oapp.framework.api.metadata.meta.region.scrollspy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;

/**
 * Клиентская модель элемента scrollspy-региона
 */
@Getter
@Setter
public class SingleScrollspyElement extends ScrollspyElement {

    @JsonProperty
    private List<CompiledRegionItem> content;

    @Override
    public void collectWidgets(List<Widget<?>> compiledWidgets) {
        collectWidgets(content, compiledWidgets);
    }
}

package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;

/**
 * Клиентская модель региона n2o
 */
@Getter
@Setter
public abstract class Region extends Component implements CompiledRegionItem, SrcAware, IdAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private List<CompiledRegionItem> content;

    @Override
    public void collectWidgets(List<Widget<?>> compiledWidgets) {
        collectWidgets(content, compiledWidgets);
    }
}

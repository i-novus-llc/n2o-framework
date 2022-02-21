package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Extractable;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;
import java.util.Map;

/**
 * Модель региона 2.0
 */
@Getter
@Setter
public abstract class N2oRegion extends N2oComponent implements SourceMetadata, Extractable {
    private String id;
    private String width;
    @Deprecated
    private String place;
    private SourceComponent[] content;

    @Override
    public String getPostfix() {
        return "region";
    }

    public String getAlias() {
        return "w";
    }

    @Override
    public void extractInWidgetList(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
        if (content != null) {
            for (SourceComponent component : content) {
                if (component instanceof Extractable)
                    ((Extractable) component).extractInWidgetList(result, ids, getAlias());
            }
        }
    }
}

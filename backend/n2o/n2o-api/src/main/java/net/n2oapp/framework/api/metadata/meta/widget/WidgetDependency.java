package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.Dependency;

import java.util.List;

/**
 * Клиентская модель зависимости.
 * fetch - зависимость master-detail
 */
@Getter
@Setter
public class WidgetDependency implements Compiled {

    @JsonProperty
    private List<Dependency> enabled;

    @JsonProperty
    private List<Dependency> visible;

    public boolean isEmpty() {
        return visible == null && enabled == null;
    }
}

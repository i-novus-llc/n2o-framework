package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;

import java.util.List;

/**
 * Клиентская модель зависимости.
 * fetch - зависимость master-detail
 */
@Getter
@Setter
public class WidgetDependency implements Compiled {
    /**
     * Ссылки на модели, при изменении которых будет срабатывать зависимость master/detail
     */
    @JsonProperty
    @Deprecated
    private List<DependencyCondition> fetch;
    @JsonProperty
    private List<DependencyCondition> visible;

    public boolean isEmpty() {
        return fetch == null && visible == null;
    }
}

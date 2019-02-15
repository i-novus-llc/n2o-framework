package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

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
    private List<Condition> fetch;
    @JsonProperty
    private List<Condition> visible;

    public boolean isEmpty() {
        return fetch == null && visible == null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Condition implements Compiled {
        @JsonProperty
        private String on;
        @JsonProperty
        private Object condition;
    }

}

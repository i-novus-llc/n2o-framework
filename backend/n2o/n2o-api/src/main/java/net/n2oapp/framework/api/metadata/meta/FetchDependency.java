package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.List;

/**
 * Клиентская модель зависимости.
 * fetch - зависимость master-detail
 */
@Getter
@Setter
public class FetchDependency implements Compiled {
    /**
     * Ссылки на модели, при изменении которых будет срабатывать зависимость master/detail
     */
    @JsonProperty
    private List<On> fetch;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class On implements Compiled {
        @JsonProperty
        private String on;
    }

}

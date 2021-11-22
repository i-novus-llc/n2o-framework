package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.io.Serializable;
import java.util.List;

/**
 * Информация об обновлении компонентов после действия
 */
@Getter
@Setter
public class RefreshSaga implements Compiled {
    @JsonProperty
    private Type type;
    @JsonProperty
    private Options options;

    public RefreshSaga() {
        this.options = new Options();
    }

    /**
     * Тип компонента
     */
    public enum Type {
        widget,
        datasource
    }

    /**
     * Настройки обновления
     */
    @Getter
    @Setter
    public static class Options implements Serializable {
        @JsonProperty
        private String widgetId;
        @JsonProperty
        private List<String> datasourcesId;
    }
}

package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.Dependency;

import java.util.List;

/**
 * Клиентская модель источника данных, ссылающегося на источник из application.xml
 */
@Getter
@Setter
public class ApplicationDatasource extends AbstractDatasource {

    @JsonProperty
    private List<Dependency> dependencies;
    @JsonProperty
    private Provider provider;

    @Getter
    @Setter
    public static class Provider {
        @JsonProperty
        private String type = "application";
        @JsonProperty
        private String datasource;
    }
}

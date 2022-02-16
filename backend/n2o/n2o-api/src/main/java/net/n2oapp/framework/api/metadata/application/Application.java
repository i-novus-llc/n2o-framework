package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.header.Header;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель приложения
 */
@Getter
@Setter
public class Application implements Compiled {
    @JsonProperty
    private Layout layout;

    @JsonProperty
    private Header header;

    @JsonProperty
    private Sidebar sidebar;

    @JsonProperty
    private Footer footer;

    @JsonProperty
    private List<Event> events;

    @JsonProperty
    private Map<String, AbstractDatasource> datasources;

    @JsonProperty
    private String wsPrefix;

    @Getter
    @Setter
    public static class Layout implements Compiled {
        @JsonProperty
        private Boolean fullSizeHeader;
        @JsonProperty
        private Boolean fixed;
    }

}

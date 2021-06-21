package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.header.Header;

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

    @Getter
    @Setter
    public static class Layout implements Compiled {
        private Boolean fullSizeHeader;
        private Boolean fixed;
    }

}

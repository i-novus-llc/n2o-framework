package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.List;

/**
 * Клиентская модель `<sub-page>`
 */
@Getter
@Setter
public class SubPageRegion extends Region {
    @JsonProperty
    private String src;
    @JsonProperty
    private String defaultPageId;
    @JsonProperty("routes")
    private List<Page> pages;

    @Getter
    @Setter
    public static class Page implements Compiled {
        @JsonProperty
        private String id;
        @JsonProperty
        private String route;
        @JsonProperty
        private String url;
    }
}

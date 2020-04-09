package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клиентская модель страницы с тремя регионами
 */
@Getter
@Setter
public class TopLeftRightPage extends StandardPage {
    @JsonProperty
    private Boolean needScrollButton;
    @JsonProperty
    private Places places;

    @Getter
    @Setter
    public static class Places implements Serializable {
        @JsonProperty
        private RegionOptions top;
        @JsonProperty
        private RegionOptions left;
        @JsonProperty
        private RegionOptions right;

        @Getter
        @Setter
        public static class RegionOptions implements Serializable {
            @JsonProperty
            private String width;
            @JsonProperty
            private Boolean fixed;
            @JsonProperty
            private Integer offset;
        }
    }
}

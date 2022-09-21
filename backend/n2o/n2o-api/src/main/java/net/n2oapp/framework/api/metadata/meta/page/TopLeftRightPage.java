package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

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
    public static class Places implements Compiled {
        @JsonProperty
        private RegionOptions top;
        @JsonProperty
        private RegionOptions left;
        @JsonProperty
        private RegionOptions right;

        @Getter
        @Setter
        public static class RegionOptions implements Compiled {
            @JsonProperty
            private String width;
            @JsonProperty
            private Boolean fixed;
            @JsonProperty
            private Integer offset;
        }
    }
}

package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.meta.region.Region;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель страницы с регионами
 */
@Getter
@Setter
public class StandardPage extends Page {
    @JsonProperty
    private Map<String, List<Region>> regions;
    @JsonProperty
    private RegionWidth width;

    @Getter
    @Setter
    @NoArgsConstructor
    public class RegionWidth implements Compiled {
        @JsonProperty
        private String left;
        @JsonProperty
        private String right;

        public RegionWidth(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
}

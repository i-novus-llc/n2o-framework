package net.n2oapp.framework.api.metadata.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;

/**
 * Клиентская модель панели поиска
 */
@Getter
@Setter
public class SearchBar implements Compiled {
    @JsonProperty
    private String urlFieldId;
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String iconFieldId;
    @JsonProperty
    private String descrFieldId;
    @JsonProperty
    private SearchPageLocation searchPageLocation;
    @JsonProperty
    private ClientDataProvider dataProvider;

    @Setter
    @Getter
    public static class SearchPageLocation implements Compiled {
        @JsonProperty
        private LinkTypeEnum linkType;
    }

    @RequiredArgsConstructor
    @Getter
    public enum LinkTypeEnum implements N2oEnum {
        INNER("inner"),
        OUTER("outer");

        private final String id;
    }
}

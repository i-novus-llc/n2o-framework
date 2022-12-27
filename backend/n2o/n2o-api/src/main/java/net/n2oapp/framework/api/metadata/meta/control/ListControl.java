package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class ListControl extends Control {
    @JsonProperty
    private String valueFieldId;
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String sortFieldId;
    @JsonProperty
    private String iconFieldId;
    @JsonProperty
    private String imageFieldId;
    @JsonProperty
    private String groupFieldId;
    @JsonProperty
    private Boolean hasSearch;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private Boolean closePopupOnSelect;
    @JsonProperty
    private String format;
    @JsonProperty
    private Boolean caching;
    @JsonProperty
    private String enabledFieldId;
    @JsonProperty
    private Badge badge;

    @JsonProperty
    private List<Map<String, Object>> data;
    @JsonProperty
    private ClientDataProvider dataProvider;
    @JsonProperty
    private String statusFieldId;
    @JsonProperty
    private String datasource;
}

package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateInterval extends Control {
    @JsonProperty
    private String dateFormat;
    @JsonProperty
    private String timeFormat;
    @JsonProperty("outputFormat")
    private String outputFormat;
    @JsonProperty
    private Boolean utc;
}

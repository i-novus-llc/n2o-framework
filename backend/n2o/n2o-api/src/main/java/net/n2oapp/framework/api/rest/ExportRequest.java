package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExportRequest {
    @JsonProperty
    private String format;
    @JsonProperty
    private String charset;
    @JsonProperty
    private String url;
    @JsonProperty
    private Map<String, String> fields;
}

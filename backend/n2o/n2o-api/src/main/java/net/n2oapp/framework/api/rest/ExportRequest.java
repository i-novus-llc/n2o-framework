package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private List<ExportField> fields;
    @JsonProperty
    private String filename;

    @Getter
    @Setter
    public static class ExportField {
        @JsonProperty
        private String id;
        @JsonProperty
        private String title;
        @JsonProperty
        private String format;
    }
}

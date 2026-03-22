package net.n2oapp.framework.ui.controller.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExternalRequest {
    @JsonProperty
    private String format;
    @JsonProperty
    private String charset;
    @JsonProperty
    private String filename;
    @JsonProperty
    private List<ExportField> fields;
    @JsonProperty
    private int page;
    @JsonProperty
    private int size;
    @JsonProperty
    private Map<String, String> sortings;
    @JsonProperty
    private List<ExportFilter> filters;

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

    @Getter
    @Setter
    public static class ExportFilter {
        @JsonProperty
        private String id;
        @JsonProperty
        private String value;
    }
}
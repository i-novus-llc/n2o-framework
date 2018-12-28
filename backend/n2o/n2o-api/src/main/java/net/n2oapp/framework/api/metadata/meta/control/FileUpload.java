package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUpload extends Control {
    @JsonProperty
    private String uploadUrl;
    @JsonProperty
    private String deleteUrl;
    @JsonProperty
    private Boolean ajax;
    @JsonProperty
    private Boolean showSize;
    @JsonProperty
    private String valueFieldId;
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String urlFieldId;
    @JsonProperty
    private Boolean multi;
}

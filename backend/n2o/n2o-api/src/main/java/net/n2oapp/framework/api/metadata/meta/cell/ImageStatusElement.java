package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

@Getter
@Setter
public class ImageStatusElement implements SrcAware, Compiled {
    @JsonProperty
    private String src;
    @JsonProperty
    private String fieldId;
    @JsonProperty
    private String icon;
    @JsonProperty
    private ImageStatusElementPlace place;
}

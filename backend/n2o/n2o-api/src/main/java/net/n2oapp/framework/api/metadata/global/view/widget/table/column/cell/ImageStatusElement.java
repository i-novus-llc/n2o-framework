package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

@Getter
@Setter
public class ImageStatusElement implements SrcAware, Source {
    @JsonProperty
    private String src;
    @JsonProperty
    private String fieldId;
    @JsonProperty
    private String icon;
    @JsonProperty
    private Place place;

    public enum Place {
        topLeft, topRight, bottomLeft, bottomRight
    }
}

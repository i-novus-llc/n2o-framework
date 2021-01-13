package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

@Getter
@Setter
public class ImageStatusElement implements SrcAware, Source {
    private String src;
    private String fieldId;
    private String icon;
    private Place place;

    public enum Place {
        topLeft, topRight, bottomLeft, bottomRight
    }
}

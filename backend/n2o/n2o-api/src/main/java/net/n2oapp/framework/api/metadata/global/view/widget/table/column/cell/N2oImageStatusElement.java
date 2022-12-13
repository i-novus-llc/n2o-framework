package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;

@Getter
@Setter
@VisualComponent
public class N2oImageStatusElement implements SrcAware, Source {
    private String src;
    @VisualAttribute
    private String fieldId;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private ImageStatusElementPlace place;
}

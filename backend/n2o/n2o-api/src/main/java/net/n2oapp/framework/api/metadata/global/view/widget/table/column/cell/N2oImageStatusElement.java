package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;

@Getter
@Setter
public class N2oImageStatusElement implements SrcAware, Source {
    private String src;
    private String fieldId;
    private String icon;
    private ImageStatusElementPlace place;
}

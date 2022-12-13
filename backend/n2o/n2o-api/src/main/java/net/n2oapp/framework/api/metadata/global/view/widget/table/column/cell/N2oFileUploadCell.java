package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;


/**
 * Ячейка загрузки файлов
 */
@Getter
@Setter
@VisualComponent
public class N2oFileUploadCell extends N2oAbstractCell {
    @VisualAttribute
    private Boolean multi;
    @VisualAttribute
    private Boolean ajax;
    @VisualAttribute
    private String uploadUrl;
    @VisualAttribute
    private String deleteUrl;
    @VisualAttribute
    private String valueFieldId;
    @VisualAttribute
    private String labelFieldId;
    @VisualAttribute
    private String messageFieldId;
    @VisualAttribute
    private String urlFieldId;
    private String requestParam;
    @VisualAttribute
    private Boolean showSize;
    @VisualAttribute
    private String accept;
    @VisualAttribute
    private String label;
    @VisualAttribute
    private String uploadIcon;
    @VisualAttribute
    private String deleteIcon;
}

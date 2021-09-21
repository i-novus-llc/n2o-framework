package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;


/**
 * Ячейка загрузки файлов
 */
@Getter
@Setter
public class N2oFileUploadCell extends N2oAbstractCell {
    private Boolean multi;
    private Boolean ajax;
    private String uploadUrl;
    private String deleteUrl;
    private String valueFieldId;
    private String labelFieldId;
    private String messageFieldId;
    private String urlFieldId;
    private String requestParam;
    private Boolean showSize;
    private String accept;
    private String label;
    private String uploadIcon;
    private String deleteIcon;
}

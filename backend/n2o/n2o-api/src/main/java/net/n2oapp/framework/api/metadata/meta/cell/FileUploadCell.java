package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель ячейки загрузки файлов
 */
@Getter
@Setter
public class FileUploadCell extends AbstractCell {
    @JsonProperty
    private String label;
    @JsonProperty
    private Boolean multi;
    @JsonProperty
    private Boolean ajax;
    @JsonProperty
    private String uploadUrl;
    @JsonProperty
    private String deleteUrl;
    @JsonProperty
    private String valueFieldId;
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String urlFieldId;
    @JsonProperty
    private String requestParam;
    @JsonProperty
    private Boolean showSize;
    @JsonProperty
    private String accept;
    @JsonProperty
    private String responseFieldId;
    @JsonProperty
    private String uploadIcon;
    @JsonProperty
    private String deleteIcon;
}

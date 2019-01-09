package net.n2oapp.framework.api.metadata.control;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N2oFileUpload extends N2oStandardField {
    private String uploadUrl;
    private String deleteUrl;
    private String valueFieldId;
    private String labelFieldId;
    private String messageFieldId;
    private String urlFieldId;
    private Boolean showSize;
    private Boolean multi;
    private Boolean ajax;
}

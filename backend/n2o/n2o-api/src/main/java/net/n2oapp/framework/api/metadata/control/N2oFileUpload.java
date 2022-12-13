package net.n2oapp.framework.api.metadata.control;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

import java.util.Map;

/**
 *  Компонент загрузки файлов
 */
@Getter
@Setter
@VisualComponent
public class N2oFileUpload extends N2oStandardField {
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
    private Boolean multi;
    @VisualAttribute
    private Boolean ajax;
    @VisualAttribute
    private Map<String, String> defValue;
    @VisualAttribute
    private String accept;
}

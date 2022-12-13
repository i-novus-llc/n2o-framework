package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.control.OutputList;

/**
 * Компонент вывода многострочного текста
 */
@Getter
@Setter
@VisualComponent
public class N2oOutputList extends N2oPlainField {
    @VisualAttribute
    private String labelFieldId;
    @VisualAttribute
    private String hrefFieldId;
    @VisualAttribute
    private Target target;
    @VisualAttribute
    private OutputList.Direction direction;
    @VisualAttribute
    private String separator;
}

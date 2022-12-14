package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.control.OutputList;

/**
 * Компонент вывода многострочного текста
 */
@Getter
@Setter
public class N2oOutputList extends N2oPlainField {
    private String labelFieldId;
    private String hrefFieldId;
    private Target target;
    private OutputList.Direction direction;
    private String separator;
}

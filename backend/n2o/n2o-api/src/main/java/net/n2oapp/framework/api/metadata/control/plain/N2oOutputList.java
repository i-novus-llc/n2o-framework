package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.control.OutputList;

/**
 * Компонент вывода многострочного текста
 */
@Getter
@Setter
public class N2oOutputList extends N2oPlainField {
    private String labelFieldId;
    private String hrefFieldId;
    private TargetEnum target;
    private OutputList.DirectionEnum direction;
    private String separator;
}

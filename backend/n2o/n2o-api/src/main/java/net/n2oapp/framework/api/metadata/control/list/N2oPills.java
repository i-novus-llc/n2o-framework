package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiListFieldAbstract;

/**
 * Компонент Таблетки
 */
@Getter
@Setter
@VisualComponent
public class N2oPills extends N2oMultiListFieldAbstract {
    @VisualAttribute
    private MultiType type;
}

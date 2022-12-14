package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiListFieldAbstract;

/**
 * Компонент Таблетки
 */
@Getter
@Setter
public class N2oPills extends N2oMultiListFieldAbstract {
    private MultiType type;
}

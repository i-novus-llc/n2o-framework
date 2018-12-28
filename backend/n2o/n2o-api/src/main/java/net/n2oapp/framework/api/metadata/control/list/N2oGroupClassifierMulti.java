package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiListFieldAbstract;

/**
 * @author dfirstov
 * @since 16.07.2015
 */
@Getter
@Setter
public class N2oGroupClassifierMulti extends N2oMultiListFieldAbstract implements InfoFieldAware, GroupFieldAware {
    private String infoFieldId;
    private String infoStyle;
    private GroupClassifierMode mode;
}

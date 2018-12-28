package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dfirstov
 * @since 16.07.2015
 */
@Getter
@Setter
public class N2oGroupClassifierSingle extends N2oSingleListFieldAbstract implements InfoFieldAware, GroupFieldAware {

    private String infoFieldId;
    private String infoStyle;


    public GroupClassifierMode getMode() {
        return GroupClassifierMode.SINGLE;
    }
}

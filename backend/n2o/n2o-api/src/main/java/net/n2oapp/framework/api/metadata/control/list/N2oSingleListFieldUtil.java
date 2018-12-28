package net.n2oapp.framework.api.metadata.control.list;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SingleListFieldSubModelQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author V. Alexeev.
 * @date 15.04.2016
 */
public class N2oSingleListFieldUtil {

    public static SubModelQuery getSubModelQuery(N2oListField listField) {
        return new SingleListFieldSubModelQuery(listField.getId(), listField.getQueryId(),
                listField.getValueFieldId(), listField.getLabelFieldId());
    }

}

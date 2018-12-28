package net.n2oapp.framework.api.metadata.control.multi;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.local.view.widget.util.MultiListFieldSubModelQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.control.ListControl;

import java.util.Collection;
import java.util.List;

public class N2oMultiListFieldUtil {

    public static boolean containsHimself(DataSet dataSet, ListControl listField) {
        Object value = dataSet.get(listField.getId());
        return value != null && value instanceof Collection && ((List) value).size() > 0;
    }



    public static SubModelQuery getSubModelQuery(N2oListField listField) {
        return new MultiListFieldSubModelQuery(listField.getId(), listField.getQueryId(),
                listField.getValueFieldId(), listField.getLabelFieldId());
    }


}

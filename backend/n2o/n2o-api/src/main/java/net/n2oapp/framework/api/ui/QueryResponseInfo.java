package net.n2oapp.framework.api.ui;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;

import java.util.Collection;

/**
 * @author operehod
 * @since 15.06.2015
 */
public class QueryResponseInfo extends ResponseInfo {

    public void prepare(Collection<DataSet> collection) {
        if (collection != null && collection.size() == 1)
            getMessageList().forEach(m -> {
                String msg = StringUtils.resolveLinks(m.getText(), collection.iterator().next());
                m.setText(msg);
            });
    }

}

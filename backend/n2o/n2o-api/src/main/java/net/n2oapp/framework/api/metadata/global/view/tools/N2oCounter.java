package net.n2oapp.framework.api.metadata.global.view.tools;

import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.io.Serializable;
import java.util.List;

/**
 * @author iryabov
 * @since 11.11.2014
 */
public class N2oCounter implements Serializable {
    //    private String queryId;
   N2oPreFilter[] preFilters;

    public N2oPreFilter[] getPreFilters() {
        return preFilters;
    }

    public void setPreFilters(N2oPreFilter[] preFilters) {
        this.preFilters = preFilters;
    }
}

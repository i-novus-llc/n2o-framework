package net.n2oapp.framework.api.metadata.local.view.widget.util;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;

@Getter
@Setter
public abstract class SubModelQuery implements Serializable {

    private String subModel;
    private String queryId;
    private String valueFieldId;
    private String labelFieldId;
    private Boolean multi;

    public SubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId, Boolean multi) {
        this.subModel = subModel;
        this.queryId = queryId;
        this.valueFieldId = valueFieldId;
        this.labelFieldId = labelFieldId;
        this.multi = multi;
    }

    public abstract void applySubModel(Map<String, Object> dataSet,
                                       CompiledQuery subQuery,
                                       BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> queryExecutor);
}

package net.n2oapp.framework.api.metadata.local.view.widget.util;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * User: operehod
 * Date: 26.01.2015
 * Time: 17:48
 */
public interface SubModelQuery extends Serializable {


    String getSubModel();

    void applySubModel(Map<String, Object> dataSet,
                              Function<String, CompiledQuery> querySupplier,
                              BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> queryExecutor);
}

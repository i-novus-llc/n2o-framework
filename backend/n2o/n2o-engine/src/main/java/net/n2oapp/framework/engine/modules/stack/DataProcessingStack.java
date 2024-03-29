package net.n2oapp.framework.engine.modules.stack;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class DataProcessingStack {

    Logger logger = LoggerFactory.getLogger(getClass());
    private volatile List<DataProcessing> stack;

    public void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        process(module -> {
            logger.debug("Processing in data-set");
            module.processAction(requestInfo, responseInfo, dataSet);
        });
    }

    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet, N2oException exception) {
        process(module -> {
            logger.debug("Processing action error");
            module.processActionError(requestInfo, responseInfo, dataSet, exception);
        });
    }

    public void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        process(module -> {
            logger.debug("Processing out data-set");
            module.processActionResult(requestInfo, responseInfo, dataSet);
        });
    }

    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        process(module -> {
            logger.debug("Processing query");
            module.processQuery(requestInfo, responseInfo);
        });
    }

    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
        process(module -> {
            logger.debug("Processing query error");
            module.processQueryError(requestInfo, responseInfo, exception);
        });
    }

    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        process(module -> {
            logger.debug("Processing query-result");
            module.processQueryResult(requestInfo, responseInfo, page);
        });
    }

    private void process(DataProcessingCallback callback) {
        if (stack == null)
            stack = findModules();
        for (final DataProcessing module : stack)
            callback.process(module);
    }

    protected abstract List<DataProcessing> findModules();
}

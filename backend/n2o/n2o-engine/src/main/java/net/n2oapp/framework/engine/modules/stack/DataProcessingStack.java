package net.n2oapp.framework.engine.modules.stack;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.bean.BeansOrderException;
import net.n2oapp.framework.api.bean.BeansSorting;
import net.n2oapp.framework.api.bean.LocatedBeanPack;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.aware.OriginAware;
import net.n2oapp.framework.api.processing.N2oModule;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: operhod
 * Date: 23.11.13
 * Time: 15:01
 */
public abstract class DataProcessingStack {

    Logger logger = LoggerFactory.getLogger(getClass());
    private volatile List<N2oModule> stack;

    public void processAction(final ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, final DataSet dataSet) {
        process(requestInfo.getOperation(), module -> {
            logger.debug("Processing in data-set in '{}' module", module.getId());
            module.processAction(requestInfo, responseInfo, dataSet);
        });
    }


    public void processActionError(final ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, final DataSet dataSet,
                                   final N2oException exception) {
        process(requestInfo.getOperation(), module -> {
            logger.debug("Processing action error in '{}' module", module.getId());
            module.processActionError(requestInfo, responseInfo, dataSet, exception);
        });
    }

    public void processActionResult(final ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, final DataSet dataSet) {
        process(requestInfo.getOperation(), module -> {
            logger.debug("Processing out data-set in '{}' module", module.getId());
            module.processActionResult(requestInfo, responseInfo, dataSet);
        });
    }

    public void processQuery(final QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        process(requestInfo.getQuery(), module -> {
            logger.debug("Processing query in '{}' module", module.getId());
            module.processQuery(requestInfo, responseInfo);
        });
    }

    public void processQueryError(final QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, final N2oException exception) {
        process(requestInfo.getQuery(), module -> {
            logger.debug("Processing query error in '{}' module", module.getId());
            module.processQueryError(requestInfo, responseInfo, exception);
        });
    }

    public void processQueryResult(final QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, final CollectionPage<DataSet> page) {
        process(requestInfo.getQuery(), module -> {
            logger.debug("Processing query-result in '{}' module", module.getId());
            module.processQueryResult(requestInfo, responseInfo, page);
        });
    }


    private void process(OriginAware originAware, final DataProcessingCallback callback) {
        if (stack == null)
            initStack();
        for (final N2oModule module : stack) {
            if (!module.isDisable()) {
                callback.process(module);
            } else logger.debug("Module '{}' is disable!", module.getId());
        }
    }

    public void disableModule(String module) {
        for (N2oModule n2oModule : stack) {
            if (n2oModule.getId().equals(module)) {
                n2oModule.setDisable(true);
                return;
            }
        }
    }


    public void enableModule(String module) {
        for (N2oModule n2oModule : stack) {
            if (n2oModule.getId().equals(module)) {
                n2oModule.setDisable(false);
                return;
            }
        }
    }

    private synchronized void initStack() {
        List<N2oModule> modules = findModules();
        List<LocatedBeanPack<N2oModule>> packs = findModulePacks();
        try {
            if (stack == null) stack = BeansSorting.sort(modules, packs);
        } catch (BeansOrderException e) {
            throw new DataProcessingStackException("Incorrect n2o-modules order registration");
        }
    }

    private UserContext getUser() {
        return StaticUserContext.getUserContext();
    }

    protected abstract List<N2oModule> findModules();

    protected abstract List<LocatedBeanPack<N2oModule>> findModulePacks();

}

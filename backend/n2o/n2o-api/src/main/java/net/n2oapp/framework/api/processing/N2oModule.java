package net.n2oapp.framework.api.processing;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.bean.LocatedBean;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.user.UserContext;

/**
 * Абстрактная реализация обработки вызовов действий и выборок N2O
 */
public abstract class N2oModule implements DataProcessing, LocatedBean {

    protected String id;
    protected boolean disable;

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    //логика для задания положения модуля среди других

    private N2oModule[] prevBeans;
    private N2oModule[] nextBeans;
    private boolean beforeAll;
    private boolean afterAll;

    @Override
    public N2oModule[] getNextBeans() {
        return nextBeans;
    }

    @Override
    public N2oModule[] getPrevBeans() {
        return prevBeans;
    }

    @Override
    public boolean isBeforeAll() {
        return beforeAll;
    }

    @Override
    public boolean isAfterAll() {
        return afterAll;
    }


    public void setBeforeAll(boolean beforeAll) {
        this.beforeAll = beforeAll;
    }

    public void setAfterAll(boolean afterAll) {
        this.afterAll = afterAll;
    }

    public void setBefore(N2oModule... n2oModule) {
        this.nextBeans = n2oModule;
    }

    public void setAfter(N2oModule... n2oModule) {
        this.prevBeans = n2oModule;
    }

    @Override
    public void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {

    }

    @Override
    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet, N2oException exception) {

    }

    @Override
    public void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {

    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {

    }

    @Override
    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {

    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {

    }
}

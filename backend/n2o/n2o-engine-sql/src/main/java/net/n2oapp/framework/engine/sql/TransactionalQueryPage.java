package net.n2oapp.framework.engine.sql;

import net.n2oapp.criteria.api.ComputationalCollectionPage;
import net.n2oapp.routing.datasource.JndiRoutingDataSourceTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.Collection;

/**
 * User: operehod
 * Date: 17.11.2014
 * Time: 11:22
 */
public class TransactionalQueryPage<T> extends ComputationalCollectionPage<T> {

    private JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate;
    private String dataSource;
    private ComputationalCollectionPage<T> queryPage;

    public TransactionalQueryPage(ComputationalCollectionPage<T> queryPage, JndiRoutingDataSourceTemplate template, String dataSource) {
        super(queryPage.getCriteria());
        this.queryPage = queryPage;
        this.jndiRoutingDataSourceTemplate = template;
        this.dataSource = dataSource;
    }

    @Override
    public void init() {
        jndiRoutingDataSourceTemplate.execute(dataSource, new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                TransactionalQueryPage.super.init();
            }
        });
    }

    @Override
    public int getCount() {
        return jndiRoutingDataSourceTemplate.execute(dataSource, transactionStatus -> {
            return TransactionalQueryPage.super.getCount();
        });
    }


    @Override
    public Collection<T> getCollectionInitial() {
        return queryPage.getCollectionInitial();
    }

    @Override
    public int getCountInitial() {
        return queryPage.getCountInitial();
    }


    @Override
    public Collection<Integer> getIdsInitial() {
        return queryPage.getIdsInitial();
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}

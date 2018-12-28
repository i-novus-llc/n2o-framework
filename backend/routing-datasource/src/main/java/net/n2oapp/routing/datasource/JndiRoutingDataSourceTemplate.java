package net.n2oapp.routing.datasource;

import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 13:52
 */
public class JndiRoutingDataSourceTemplate {
    private TransactionTemplate transactionTemplate;

    public <T> T execute(String jndiName, RoutingDataSourceCallback<T> action) {
        JndiContextHolder.setJndiContext(jndiName);
        try {
            return action.onRouting();
        } finally {
            JndiContextHolder.clearJndiContext();
        }
    }

    public <T> T execute(String jndiName, final TransactionCallback<T> action) {
        if (transactionTemplate == null) throw new IllegalStateException("transactionTemplate is null");
        return execute(jndiName, () -> {
            return transactionTemplate.execute(action);
        });
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}

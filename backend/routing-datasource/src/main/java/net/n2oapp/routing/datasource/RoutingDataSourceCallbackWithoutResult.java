package net.n2oapp.routing.datasource;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 14:25
 */
public abstract class RoutingDataSourceCallbackWithoutResult implements RoutingDataSourceCallback<Object> {
    public void onRoutingWithoutResult() {
        onRouting();
    }
}

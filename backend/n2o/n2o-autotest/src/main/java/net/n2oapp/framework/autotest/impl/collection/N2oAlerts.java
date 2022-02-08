package net.n2oapp.framework.autotest.impl.collection;

import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oAlert;

public class N2oAlerts extends N2oComponentsCollection implements Alerts {

    @Override
    public Alert alert(int index) {
        return new N2oAlert(elements().get(index));
    }
}

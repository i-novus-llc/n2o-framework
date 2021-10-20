package net.n2oapp.framework.api.metadata.event.action;


/**
 * Reader для события <validate>
 */
public class N2OValidateAction extends N2oAbstractAction implements N2oAction {
    private static final String DEFAULT_SRC = "perform";

    public N2OValidateAction() {
        setSrc(DEFAULT_SRC);
    }

}

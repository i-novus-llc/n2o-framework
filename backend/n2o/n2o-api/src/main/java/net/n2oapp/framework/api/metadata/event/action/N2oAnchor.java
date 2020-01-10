package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

import java.io.Serializable;

/**
 * Исходная модель ccылки
 */
@Getter
@Setter
public class N2oAnchor extends N2oAbstractAction implements N2oAction {
    private String href;
    private Target target;
    private Param[] pathParams;
    private Param[] queryParams;

    public N2oAnchor(){

    }

    public N2oAnchor(String href) {
        this.href = href;
    }

    @Getter
    @Setter
    public static class Param implements Serializable {
        private String name;
        private String value;
    }


}

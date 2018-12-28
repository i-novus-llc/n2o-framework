package net.n2oapp.framework.api.metadata.event.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.n2oapp.framework.api.metadata.global.view.action.control.OperationIdAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.local.context.OutOfRangeException;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Исходная модель ccылки
 */
public class N2oAnchor extends N2oAbstractAction implements N2oAction {
    private String href;
    private Target target;

    public N2oAnchor(){

    }

    public N2oAnchor(String href) {
        this.href = href;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    @JsonIgnore
    public String getOperationId() {
        return "read";
    }


}

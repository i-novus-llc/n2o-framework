package net.n2oapp.framework.api.metadata.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;

/**
 * Абстрактное действие, содержащее стандартные саги
 */
@Getter
@Setter
public abstract class N2oAbstractMetaAction extends N2oAbstractAction {
    private Boolean closeOnSuccess;
    private Boolean doubleCloseOnSuccess;
    private Boolean closeOnFail;
    private String redirectUrl;
    private TargetEnum redirectTarget;
    private Boolean refreshOnSuccess;
    @JsonProperty("refreshDatasources")
    private String[] refreshDatasourceIds;
}

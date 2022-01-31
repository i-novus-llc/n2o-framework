package net.n2oapp.framework.api.metadata.meta.action.refresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

/**
 * Клиентская модель компонента Refresh
 */
@Getter
@Setter
public class RefreshPayload implements ActionPayload {
    @JsonProperty
    private String datasource;
}

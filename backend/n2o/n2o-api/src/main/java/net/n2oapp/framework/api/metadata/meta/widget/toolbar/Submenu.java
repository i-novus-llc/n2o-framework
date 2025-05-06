package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель субменю в меню
 */

@Getter
@Setter
public class Submenu extends AbstractButton {
    @JsonProperty("subMenu")
    private List<PerformButton> buttonList;
    @JsonProperty
    private Boolean showToggleIcon;
}

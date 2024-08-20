package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;

/**
 * Клиентская модель боковой панели
 */
@Getter
@Setter
public class Sidebar extends Component {
    @JsonProperty
    private Logo logo;
    @JsonProperty
    private SimpleMenu menu;
    @JsonProperty
    private SimpleMenu extraMenu;
    @JsonProperty
    private SidebarSide side;
    @JsonProperty
    private String path;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private String subtitle;
    @JsonProperty
    private SidebarState defaultState;
    @JsonProperty
    private SidebarState toggledState;
    @JsonProperty
    private Boolean toggleOnHover;
    @JsonProperty
    private Boolean overlay;
}

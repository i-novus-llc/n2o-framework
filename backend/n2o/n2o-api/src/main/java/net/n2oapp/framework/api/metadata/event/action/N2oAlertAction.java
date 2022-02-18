package net.n2oapp.framework.api.metadata.event.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Действие уведомления
 */
@Getter
@Setter
public class N2oAlertAction extends N2oAbstractAction {

    @JsonProperty
    private String title;
    @JsonProperty
    private String text;
    @JsonProperty
    private String href;
    @JsonProperty
    private String color;
    @JsonProperty
    private String style;
    @JsonProperty
    private String cssClass;
    @JsonProperty
    private String time;
    @JsonProperty
    private String timeout;
    @JsonProperty
    private String placement;
    @JsonProperty
    private Boolean closeButton;

}

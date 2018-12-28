package net.n2oapp.framework.api.metadata.meta.action.show_modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель коомпонента show-modal
 */
@Getter
@Setter
public class ShowModalPayload implements ActionPayload {
    @JsonProperty
    @Deprecated
    private String name;
    @JsonProperty
    private String pageId;
    @JsonProperty
    private String pageUrl;
    @JsonProperty
    private Map<String, BindLink> pathMapping;
    @JsonProperty
    private Map<String, BindLink> queryMapping;
    @JsonProperty
    private String title;
    @JsonProperty
    private String size;
    @JsonProperty
    private Boolean closeButton = true;
    @JsonProperty
    private Boolean visible = true;
    @JsonProperty
    private Map<String, Action> actions;
    @JsonProperty
    private List<Group> toolbar;
}

package net.n2oapp.framework.api.metadata.meta.action.open_drawer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
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
public class OpenDrawerPayload implements ActionPayload {
    @JsonProperty
    @Deprecated
    private String name;
    @JsonProperty
    private String pageId;
    @JsonProperty
    private String pageUrl;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
    @JsonProperty
    private String title;
    @JsonProperty
    private Map<String, Action> actions;
    @JsonProperty
    private List<Group> toolbar;
    @JsonProperty
    private String mode = "drawer";
    @JsonProperty
    private Boolean closable = true;
    @JsonProperty
    private Boolean backdrop;
    @JsonProperty
    private String width;
    @JsonProperty
    private String height;
    @JsonProperty
    private String placement;
    @JsonProperty
    private String level;
    @JsonProperty
    private Boolean closeOnBackdrop;
    @JsonProperty
    private Boolean prompt;
    @JsonProperty
    private Boolean fixedFooter;
    @JsonProperty
    private Boolean closeOnEscape;
}

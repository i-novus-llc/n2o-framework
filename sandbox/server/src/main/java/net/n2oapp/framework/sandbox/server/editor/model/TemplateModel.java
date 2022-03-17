package net.n2oapp.framework.sandbox.server.editor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Шаблон
 */
@Getter
@Setter
public class TemplateModel {

    @JsonProperty
    private String projectId;

    @JsonProperty
    private String templateId;

    @JsonProperty
    private String name;

    public String getProjectId() {
        return projectId != null ? projectId : templateId.replace('/', '_');
    }
}

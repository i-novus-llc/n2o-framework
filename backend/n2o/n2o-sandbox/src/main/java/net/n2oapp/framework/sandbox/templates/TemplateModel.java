package net.n2oapp.framework.sandbox.templates;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Шаблон проекта
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

    @JsonGetter("projectId")
    public String getProjectId() {
        return projectId != null ? projectId : templateId.replace('/', '_');
    }
}

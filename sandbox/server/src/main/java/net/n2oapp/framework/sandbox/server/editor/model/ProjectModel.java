package net.n2oapp.framework.sandbox.server.editor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectModel {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String viewUrl;
    @JsonProperty
    private List<FileModel> files;

}

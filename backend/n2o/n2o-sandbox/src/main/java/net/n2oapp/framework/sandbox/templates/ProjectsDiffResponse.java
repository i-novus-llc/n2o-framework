package net.n2oapp.framework.sandbox.templates;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.sandbox.file_storage.model.ProjectModel;

import java.util.Map;

@Getter
@Setter
public class ProjectsDiffResponse {
    boolean reload;
    Map<String, ProjectModel> projects;
}

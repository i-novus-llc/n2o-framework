package net.n2oapp.framework.sandbox.server.editor;

import net.n2oapp.framework.sandbox.server.editor.model.ProjectModel;
import net.n2oapp.framework.sandbox.server.editor.model.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class EditorController {

    @Autowired
    private ApiController api;
    @Autowired
    private TemplatesHolder templatesHolder;

    @GetMapping({"/new/{projectId}", "/new"})
    public String newProject(@PathVariable(required = false) String projectId) throws IOException {
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        ProjectModel project = api.createProject(templateModel);
        return "redirect:/editor/" + project.getId() + "/";
    }

    @GetMapping({"/clone/{projectId}", "/clone"})
    public String cloneProject(@PathVariable String projectId, HttpSession session) {
        ProjectModel project = api.cloneProject(projectId, session);
        return "redirect:/editor/" + project.getId() + "/";
    }
}

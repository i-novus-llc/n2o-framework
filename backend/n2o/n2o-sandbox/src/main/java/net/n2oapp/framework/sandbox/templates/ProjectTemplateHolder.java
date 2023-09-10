package net.n2oapp.framework.sandbox.templates;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.api.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ProjectTemplateHolder {

    @Value("${n2o.sandbox.templateCategories:/menu/examples.json,/menu/uxcomponents.json,/menu/interactions.json,/menu/uxcases.json,/menu/versions.json,/menu/applications.json}")
    private List<String> templateCategories;

    private List<CategoryModel> categoryModels = null;

    public List<CategoryModel> getProjectTemplates() {
        if (categoryModels == null) loadTemplates();
        return categoryModels;
    }

    public TemplateModel getTemplateModel(String projectId) {
        if (StringUtils.isEmpty(projectId)) return null;
        return categoryModels.stream().flatMap(cm -> cm.getSections().stream()).flatMap(sn -> sn.getTemplates().stream())
                .filter(te -> projectId.equals(te.getProjectId())).findAny().orElse(null);
    }

    @EventListener(ApplicationReadyEvent.class)
    private void loadTemplates() {
        if (categoryModels == null) {
            categoryModels = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            for (String category : templateCategories) {
                ClassPathResource resource = new ClassPathResource(category);
                try {
                    categoryModels.add(objectMapper.readValue(resource.getInputStream(),
                            objectMapper.getTypeFactory().constructType(CategoryModel.class)));
                } catch (IOException e) {
                    log.error("error while template loading {}", category, e);
                }
            }
        }
    }

}

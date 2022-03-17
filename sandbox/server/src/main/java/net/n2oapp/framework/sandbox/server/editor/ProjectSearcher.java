package net.n2oapp.framework.sandbox.server.editor;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.sandbox.server.editor.model.SearchProjectModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Компонент поиска совпадений в проектах по входной строке
 */
@Component
@Slf4j
public class ProjectSearcher {

    private static final int PROJECT_LIMIT = 30;
    private static final int PROJECT_MATCHES_LIMIT = 10;

    @Value("${n2o.sandbox.projectSearchFolders:/examples,/data,/access,/routing,/cases,/uxcomponents,/uxcases}")
    private List<String> projectFolders;

    @Autowired
    private ResourceLoader resourceLoader;


    List<SearchProjectModel> search(String text) throws URISyntaxException, IOException {
        Map<String, SearchProjectModel> projectsMap = new LinkedHashMap<>();

        for (String folder : projectFolders) {
            Resource[] resources = ResourcePatternUtils
                    .getResourcePatternResolver(resourceLoader)
                    .getResources("classpath*:" + folder + "/**");

            for (Resource resource : resources) {
                if (resource.isReadable() && !resource.getFilename().endsWith(".json")) {
                    String projectId = getProjectId(resource.getURL().toString(), folder);
                    if (!projectsMap.containsKey(projectId) && projectsMap.size() == PROJECT_LIMIT)
                        break;
                    else if (projectsMap.containsKey(projectId) && projectsMap.get(projectId).getItems().size() == PROJECT_MATCHES_LIMIT)
                        continue;

                    try (Stream<String> lines = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines()) {
                        List<String> linesList = lines.collect(Collectors.toList());
                        for (int i = 0; i < linesList.size(); i++) {
                            if (StringUtils.containsIgnoreCase(linesList.get(i), text)) {
                                if (!projectsMap.containsKey(projectId))
                                    projectsMap.put(projectId, new SearchProjectModel(projectId, new ArrayList<>()));

                                projectsMap.get(projectId).getItems().add(
                                        new SearchProjectModel.Item(
                                                getFileName(resource),
                                                linesList.get(i).trim(),
                                                i + 1));

                                if (projectsMap.get(projectId).getItems().size() == PROJECT_MATCHES_LIMIT)
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        log.error(String.format("Error while reading file: %s\\%s%n%s", folder, getFileName(resource), e.getMessage()));
                    }
                }
            }
        }
        return new ArrayList<>(projectsMap.values());
    }

    private String getProjectId(String file, String folder) {
        return file.substring(file.indexOf(folder) + 1, file.lastIndexOf("/")).replace("/", "_");
    }

    private String getFileName(Resource resource) throws IOException {
        return resource.getURL().toString().substring(resource.getURL().toString().lastIndexOf("/") + 1);
    }
}

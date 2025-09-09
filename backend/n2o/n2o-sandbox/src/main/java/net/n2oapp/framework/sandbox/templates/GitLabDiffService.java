package net.n2oapp.framework.sandbox.templates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GitLabDiffService {
    private final Logger logger = LoggerFactory.getLogger(GitLabDiffService.class);
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    @Value("${project.id:framework%2Fn2o-framework}")
    private String projectId;
    @Value("${gitlab.api:https://git.i-novus.ru/api/v4}")
    private String gitlabApi;
    private String token;

    public GitLabDiffService() {
        this.mapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
        this.token = System.getenv("CI_JOB_TOKEN");
        if (this.token == null || this.token.isBlank()) {
            logger.error("CI_JOB_TOKEN not set in environment");
        }
    }

    public void collectChangedPackages(String resourcesPath,
                                       String oldTag,
                                       String newTag,
                                       Set<String> maybeDeletedPackages,
                                       Set<String> changedPackages) throws IOException, InterruptedException {

        String compareUrl = String.format("%s/projects/%s/repository/compare?from=%s&to=%s",
                gitlabApi, projectId, oldTag, newTag);

        JsonNode json = callApi(compareUrl);
        JsonNode diffs = json.get("diffs");

        if (diffs == null) return;

        for (JsonNode diff : diffs) {
            String oldPath = diff.get("old_path").asText();
            String newPath = diff.get("new_path").asText();

            // смотрим только файлы под resourcesPath
            String candidatePath = null;
            if (newPath != null && newPath.startsWith(resourcesPath)) {
                candidatePath = newPath;
            } else if (oldPath != null && oldPath.startsWith(resourcesPath)) {
                candidatePath = oldPath;
            }
            if (candidatePath == null) continue;

            String pkg = extractDirectory(resourcesPath, candidatePath);
            if (pkg == null || pkg.isBlank()) continue;

            boolean deleted = diff.get("deleted_file").asBoolean(false);
            if (deleted) {
                maybeDeletedPackages.add(pkg);
            } else {
                changedPackages.add(pkg);
            }
        }

        changedPackages.remove("menu");
    }

    public List<String> listFilesAtTag(String tag, String resourcesPath) throws IOException, InterruptedException {
        List<String> files = new ArrayList<>();
        String treeUrl = String.format("%s/projects/%s/repository/tree?ref=%s&recursive=true&per_page=100",
                gitlabApi, projectId, tag);

        JsonNode json = callApi(treeUrl);
        for (JsonNode fileNode : json) {
            if ("blob".equals(fileNode.get("type").asText())) {
                String path = fileNode.get("path").asText();
                if (path.startsWith(resourcesPath)) {
                    files.add(path);
                }
            }
        }
        return files;
    }

    private static String extractDirectory(String basePath, String fullPath) {
        String normalized = fullPath.replace('\\', '/');
        String base = basePath.replace('\\', '/');
        if (!normalized.startsWith(base)) {
            return null;
        }
        String remainder = normalized.substring(base.length());
        if (remainder.startsWith("/")) {
            remainder = remainder.substring(1);
        }
        int lastSlash = remainder.lastIndexOf('/');
        if (lastSlash <= 0) {
            return null;
        }
        return remainder.substring(0, lastSlash);
    }

    private JsonNode callApi(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("GitLab API failed: " + response.statusCode() + "\n" + response.body());
        }
        return mapper.readTree(response.body());
    }

}

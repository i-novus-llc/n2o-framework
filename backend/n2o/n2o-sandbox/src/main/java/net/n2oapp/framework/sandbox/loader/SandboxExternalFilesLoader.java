package net.n2oapp.framework.sandbox.loader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.util.ExternalFilesLoader;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.templates.TemplateModel;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;
import static net.n2oapp.framework.sandbox.utils.FileUtil.isTemplate;

@Component
public class SandboxExternalFilesLoader implements ExternalFilesLoader {

    private ProjectTemplateHolder templatesHolder;
    private FileStorage fileStorage;

    public SandboxExternalFilesLoader(ProjectTemplateHolder templatesHolder, FileStorage fileStorage) {
        this.templatesHolder = templatesHolder;
        this.fileStorage = fileStorage;
    }

    @Override
    public String getContentByUri(String uri) {
        try {
            String projectId = ThreadLocalProjectId.getProjectId();
            if (isTemplate(projectId)) {
                TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
                List<FileModel> files = findResources(templateModel.getTemplateId());
                Optional<FileModel> first = files.stream().filter(f -> f.getFile().equals(uri)).findFirst();
                if (first.isPresent())
                    return first.get().getSource();
                throw new N2oException(String.format("File %s not found", uri));
            } else {
                String response = fileStorage.getFileContent(projectId, uri.substring(uri.lastIndexOf("/") + 1));
                if (response != null)
                    return response;
            }
        } catch (HttpClientErrorException.NotFound e) {
            ClassPathResource classPathResource = new ClassPathResource(uri);
            if (classPathResource.exists()) {
                try {
                    return IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    throw new N2oException(ex);
                }
            }
        }
        throw new N2oException(String.format("File %s not found", uri));
    }
}

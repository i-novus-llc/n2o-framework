package net.n2oapp.framework.sandbox.file_storage;

import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.config.register.storage.PathUtil.convertRootPathToUrl;

public class FileStorageOnDisk implements FileStorage {

    @Value("${n2o.config.path}")
    private String basePath;

    @Override
    public void saveFile(String projectId, String file, String source) {
        String fileFolder = getProjectPath(projectId) + "/" + file;
        FileSystemUtil.saveContentToFile(source, new File(fileFolder));
    }

    @Override
    public String getFileContent(String projectId, String file) {
        String uri = convertRootPathToUrl(getProjectPath(projectId) + "/" + file);
        return FileSystemUtil.getContentByUri(uri, false);
    }

    @Override
    public List<FileModel> getProjectFiles(String projectId) {
        String uri = convertRootPathToUrl(getProjectPath(projectId));
        return findFilesByUri(uri);
    }

    @Override
    public boolean isProjectExists(String projectId) {
        return Paths.get(getProjectPath(projectId) + "/index.page.xml").toFile().exists();
    }

    /**
     * Получение файлов по URI
     *
     * @param uri URI файлов
     * @return Список моделей файлов
     */
    private List<FileModel> findFilesByUri(String uri) {
        List<FileModel> files = new ArrayList<>();
        String pattern = PathUtil.convertUrlToPattern(uri, "*", "*");
        List<Node> nodes = FileSystemUtil.getNodesByLocationPattern(pattern);
        for (Node node : nodes) {
            FileModel file = new FileModel().setFile(node.getLocalPath()).setSource(node.retrieveContent());
            if (node.getLocalPath().endsWith("index.page.xml"))
                files.add(0, file);
            else
                files.add(file);
        }
        return files;
    }

    private String getProjectPath(String projectId) {
        return basePath + "/" + projectId;
    }
}


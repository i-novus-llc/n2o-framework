package net.n2oapp.framework.sandbox.cases.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class FileStorageController {

    private final Path path;

    private static final String DEFAULT_STORE_KEY = "common";
    private static final Logger log = LoggerFactory.getLogger(FileStorageController.class);
    private final Map<String, Map<String, FileModel>> storage = new HashMap<>();
    private int id = 0;

    public FileStorageController() throws IOException {
        path = Files.createTempDirectory("upload");
        path.toFile().deleteOnExit();
        storage.put(DEFAULT_STORE_KEY, new HashMap<>());
    }

    public ListResponse getList() {
        return getList(DEFAULT_STORE_KEY);
    }

    @Synchronized
    public ListResponse getList(String storeKey) {
        Map<String, FileModel> filesMap = storage.get(getStoreKeyOrDefault(storeKey));
        return new ListResponse(filesMap == null ? new ArrayList<>() : filesMap.values());
    }

    public void delete(String id) {
        delete(id, DEFAULT_STORE_KEY);
    }

    @Synchronized
    public void delete(String id, String storeKey) {
        if (id == null) return;
        if (storeKey == null) {
            Optional<Map.Entry<String, Map<String, FileModel>>> storeWithFile = storage.entrySet().stream().filter(k -> k.getValue().containsKey(id)).findFirst();
            if (storeWithFile.isEmpty()) return;
            storeKey = storeWithFile.get().getKey();
        }
        FileModel model = storage.get(storeKey).remove(id);
        if (model == null) return;
        try {
            Path filePath = getFilePath(model.getFileName(), storeKey).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileModel storeFile(MultipartFile file, String url) {
        return storeFile(file, url, DEFAULT_STORE_KEY);
    }

    @Synchronized
    public FileModel storeFile(MultipartFile file, String url, String storeKey) {
        try {
            String currentStoreKey = getStoreKeyOrDefault(storeKey);
            Path storePath = path.resolve(currentStoreKey);
            if (!storePath.toFile().exists())
                Files.createDirectory(storePath);
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = getFilePath(fileName, currentStoreKey);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            targetLocation.toFile().deleteOnExit();

            String uri = UriComponentsBuilder.fromPath(fileName).toUriString();
            FileModel model = new FileModel("" + (++id), fileName, url + "/" + uri, file.getSize(), LocalDateTime.now());
            storage.computeIfAbsent(currentStoreKey, k -> new HashMap<>());
            storage.get(currentStoreKey).put("" + id, model);

            return model;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Resource loadFile(String fileName) {
        return loadFile(fileName, DEFAULT_STORE_KEY);
    }

    @Synchronized
    public Resource loadFile(String fileName, String storeKey) {
        try {
            Path filePath = getFilePath(fileName, getStoreKeyOrDefault(storeKey)).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            return resource.exists() ? resource : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Synchronized
    public void clear() {
        storage.forEach((storeKey, filesMap) -> {
            for (String fileId : List.copyOf(filesMap.keySet()))
                delete(fileId, storeKey);
            try {
                Files.deleteIfExists(path.resolve(storeKey));
            } catch (IOException e) {
                log.error("Не удалось удалить директорию '{}'", storeKey, e);
            }
        });
        storage.clear();
        storage.put(DEFAULT_STORE_KEY, new HashMap<>());
        id = 0;
    }

    @Getter
    @AllArgsConstructor
    static class ListResponse {
        @JsonProperty
        final Collection<FileModel> files;
    }

    private String getStoreKeyOrDefault(String storeKey) {
        return storeKey == null ? DEFAULT_STORE_KEY : storeKey;
    }

    private Path getFilePath(String fileName, String storeKey) {
        return path.resolve(storeKey).resolve(fileName);
    }
}

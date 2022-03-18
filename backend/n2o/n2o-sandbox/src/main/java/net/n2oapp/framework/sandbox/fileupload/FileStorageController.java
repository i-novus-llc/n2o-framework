package net.n2oapp.framework.sandbox.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Synchronized;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileStorageController {

    private Path path;

    private Map<String, FileModel> storage = new HashMap<>();
    private int id = 0;

    public FileStorageController() throws IOException {
        path = Files.createTempDirectory("upload");
        path.toFile().deleteOnExit();
    }

    @SuppressWarnings("unused")
    public ListResponse getList() {
        return new ListResponse(storage.values());
    }

    public void delete(String id) {
        if (id != null) {
            FileModel model = storage.remove(id);
            if (model != null) {
                try {
                    Path filePath = path.resolve(model.getFileName()).normalize();
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Synchronized
    public FileModel storeFile(MultipartFile file, String url) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            Path targetLocation = path.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            targetLocation.toFile().deleteOnExit();

            String uri = ServletUriComponentsBuilder.fromPath(fileName).toUriString();
            FileModel model = new FileModel("" + (++id), fileName, url + "/" + uri, file.getSize());
            storage.put("" + id, model);

            return model;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path filePath = path.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Synchronized
    public void clear() {
        storage.forEach((k, v) -> delete(k));
    }

    public int size() {
        return storage.size();
    }

    @Getter
    @AllArgsConstructor
    static class ListResponse {
        @JsonProperty
        final Collection<FileModel> files;
    }

}

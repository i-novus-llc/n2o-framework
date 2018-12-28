package net.n2oapp.framework.examples.fileupload.controller;

import lombok.Synchronized;
import net.n2oapp.framework.examples.fileupload.model.FileModel;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

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
    public Collection<FileModel> getList() {
        return storage.values().stream().filter(FileModel::isStored).collect(Collectors.toList());
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

    @SuppressWarnings("unused")
    public void submit(List<String> ids) {
        if (ids != null) {
            for (String id : ids) {
                FileModel model = storage.get(id);
                if (model != null) {
                    model.setStored(true);
                }
            }
        }
    }

    @Synchronized
    public FileModel storeFile(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            Path targetLocation = path.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            targetLocation.toFile().deleteOnExit();

            String uri = ServletUriComponentsBuilder.fromPath(fileName).toUriString();
            FileModel model = new FileModel("" + (++id), fileName, uri);
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

}

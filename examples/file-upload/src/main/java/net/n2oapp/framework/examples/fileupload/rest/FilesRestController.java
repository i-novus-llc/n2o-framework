package net.n2oapp.framework.examples.fileupload.rest;

import jakarta.servlet.http.HttpServletRequest;
import net.n2oapp.framework.examples.fileupload.controller.FileStorageController;
import net.n2oapp.framework.examples.fileupload.model.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/files")
public class FilesRestController {

    private final FileStorageController fileStorageController;

    @Autowired
    public FilesRestController(@Autowired FileStorageController fileStorageController) {
        this.fileStorageController = fileStorageController;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "")
    public ResponseEntity<FileModel> uploadFile(@RequestParam("file") MultipartFile file) {
        FileModel model = fileStorageController.storeFile(file);
        return new ResponseEntity<>(new FileModel(model.getId(), model.getFileName(), "files/"+model.getUrl()), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity<List<FileModel>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return new ResponseEntity<>(
                Arrays.stream(files)
                        .map(file -> uploadFile(file).getBody())
                        .toList(), HttpStatus.CREATED
        );
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/{id}")
    public void deleteFile(@PathVariable Integer id) {
        fileStorageController.delete(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageController.loadFile(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ignored) {}

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

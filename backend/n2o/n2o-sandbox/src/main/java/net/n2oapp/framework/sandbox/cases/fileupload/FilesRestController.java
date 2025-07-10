package net.n2oapp.framework.sandbox.cases.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
public class FilesRestController {

    private final FileStorageController fileStorageController;

    @Autowired
    public FilesRestController(@Autowired FileStorageController fileStorageController) {
        this.fileStorageController = fileStorageController;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/files/list")
    public FileStorageController.ListResponse getList() {
        return fileStorageController.getList();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/files")
    public ResponseEntity<FileModel> uploadFile(@RequestParam("file") MultipartFile file,
                                                HttpServletRequest request) {
        FileModel model = fileStorageController.storeFile(file, request.getRequestURL().toString());
        return new ResponseEntity<>(new FileModel(model.getId(), model.getFileName(), model.getUrl(), model.getSize(), model.getDate()), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/files/{id}")
    public void deleteFile(@PathVariable String id) {
        fileStorageController.delete(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request,
                                                 @RequestHeader(value = "hasAttachment", required = false) Boolean hasAttachment) {
        return getResourceResponseEntity(null, fileName, request, hasAttachment);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/stores/{storeKey}/files/list")
    public FileStorageController.ListResponse getStoreFiles(@PathVariable(required = false) String storeKey) {
        return fileStorageController.getList(storeKey);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/stores/{storeKey}/files")
    public ResponseEntity<FileModel> uploadStoreFile(@PathVariable String storeKey,
                                                     @RequestParam("file") MultipartFile file,
                                                     HttpServletRequest request) {
        FileModel model = fileStorageController.storeFile(file, request.getRequestURL().toString(), storeKey);
        return new ResponseEntity<>(new FileModel(model.getId(), model.getFileName(), model.getUrl(), model.getSize(), model.getDate()), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/stores/{storeKey}/files/{id}")
    public void deleteStoreFile(@PathVariable String storeKey,
                                @PathVariable String id) {
        fileStorageController.delete(id, storeKey);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/stores/{storeKey}/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadStoreFile(@PathVariable String storeKey,
                                                      @PathVariable String fileName, HttpServletRequest request,
                                                      @RequestHeader(value = "hasAttachment", required = false) Boolean hasAttachment) {
        return getResourceResponseEntity(storeKey, fileName, request, hasAttachment);
    }

    private ResponseEntity<Resource> getResourceResponseEntity(String storeKey, String fileName, HttpServletRequest request, Boolean hasAttachment) {
        Resource resource = fileStorageController.loadFile(fileName, storeKey);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //do nothing
        }

        if (contentType == null)
            contentType = "application/octet-stream";

        String headerValues = "filename=\"" + resource.getFilename() + "\"";
        if (Boolean.FALSE != hasAttachment)
            headerValues = "attachment; " + headerValues;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValues)
                .body(resource);
    }
}

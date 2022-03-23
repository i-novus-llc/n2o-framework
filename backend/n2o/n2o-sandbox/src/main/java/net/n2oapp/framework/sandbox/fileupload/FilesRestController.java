package net.n2oapp.framework.sandbox.fileupload;

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
@RequestMapping("/files")
public class FilesRestController {

    private final FileStorageController fileStorageController;

    @Autowired
    public FilesRestController(@Autowired FileStorageController fileStorageController) {
        this.fileStorageController = fileStorageController;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "")
    public ResponseEntity<FileModel> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        FileModel model = fileStorageController.storeFile(file, request.getRequestURL().toString());
        return new ResponseEntity<>(new FileModel(model.getId(), model.getFileName(), model.getUrl(), model.getSize()), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFile(@PathVariable String id) {
        fileStorageController.delete(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageController.loadFile(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //do nothing
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}

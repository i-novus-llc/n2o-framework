package net.n2oapp.framework.sandbox.server.fileupload;

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


/**
 * Rest сервис для загрузки файла в примере с ячейкой file-upload
 */
@RestController
@RequestMapping("/persons")
public class PersonDocRestController {

    private final PersonStorageController personStorageController;

    public PersonDocRestController(@Autowired PersonStorageController personStorageController) {
        this.personStorageController = personStorageController;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST,value = "/{personId}/files")
    public ResponseEntity<FileModel> uploadFile(@PathVariable("personId") String personId, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        FileModel model = personStorageController.storeFile(file, request.getRequestURL().toString());
        personStorageController.setDoc(personId, model);
        return new ResponseEntity<>(new FileModel(model.getId(), model.getFileName(), model.getUrl(), model.getSize()), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{personId}/files/{id}")
    public void deleteFile(@PathVariable String personId, @PathVariable String id) {
        personStorageController.deleteDoc(personId, id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{personId}/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = personStorageController.loadFile(fileName);

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

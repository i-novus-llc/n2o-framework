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

    @GetMapping("/list")
    public PersonStorageController.ListResponse getList() {
        return personStorageController.getList();
    }

    @GetMapping("/{id}")
    public PersonModel getById(@PathVariable String id) {
        return personStorageController.getById(id);
    }

    @PostMapping("/create")
    public String createPerson(@RequestBody PersonModel person) {
        return personStorageController.createPerson(person);
    }

    @PutMapping("/update")
    public void updatePerson(@RequestBody PersonModel person) {
        personStorageController.updatePerson(person);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable String id) {
        personStorageController.deletePerson(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{personId}/files")
    public ResponseEntity<FileModel> uploadFile(@PathVariable("personId") String personId,
                                                @RequestParam("file") MultipartFile file,
                                                HttpServletRequest request) {
        FileModel model = personStorageController.storeFile(file, request.getRequestURL().toString());
        personStorageController.setDoc(personId, model);
        return new ResponseEntity<>(new FileModel(model.getId(), model.getFileName(), model.getUrl(), model.getSize(), model.getDate()), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{personId}/files/{id}")
    public void deleteFile(@PathVariable String personId, @PathVariable String id) {
        personStorageController.deleteDoc(personId, id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{personId}/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String personId,
                                                 @PathVariable String fileName,
                                                 HttpServletRequest request) {
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

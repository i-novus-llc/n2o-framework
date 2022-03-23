package net.n2oapp.framework.sandbox.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * Rest сервис для загрузки файла в примере с ячейкой file-upload
 */
@RestController
@RequestMapping("/persons")
public class PersonDocRestController {

    @Autowired
    private PersonDocRestClient restClient;

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST,value = "/{personId}/files")
    public ResponseEntity<FileModel> uploadFile(@PathVariable("personId") String personId, @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(restClient.uploadFile(personId, file), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{personId}/files/{id}")
    public void deleteFile(@PathVariable String personId, @PathVariable String id) {
        restClient.deleteFile(personId, id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{personId}/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String personId, @PathVariable String fileName) {
        return new ResponseEntity<>(restClient.downloadFile(personId, fileName), HttpStatus.OK);
    }
}

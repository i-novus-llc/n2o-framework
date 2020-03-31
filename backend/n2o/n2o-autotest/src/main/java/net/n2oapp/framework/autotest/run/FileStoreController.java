package net.n2oapp.framework.autotest.run;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Контроллер файлового хранилища для автотестов
 */
@RestController
public class FileStoreController {

    private List<FileModel> fileStore = Collections.synchronizedList(new ArrayList<>());

    public void clearFileStore() {
        fileStore.clear();
    }

    public List<FileModel> getFileStore() {
        return fileStore;
    }

    @RequestMapping(method = RequestMethod.POST, value = "files")
    public ResponseEntity<FileModel> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(storeFile(file), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "multi")
    public ResponseEntity<List<FileModel>> uploadMultiFile(@RequestParam("file") MultipartFile[] file) throws IOException {
        List<FileModel> result = new ArrayList<>();
        for (MultipartFile mf : file) {
            result.add(storeFile(mf));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "files/{id}")
    public void deleteFile(@PathVariable String id) {
        for (FileModel fs : fileStore) {
            if (id.equals(fs.id)) {
                fileStore.remove(fs);
                break;
            }
        }
    }

    private FileModel storeFile(MultipartFile file) throws IOException {
        FileModel fm = FileModel.builder().id(UUID.randomUUID().toString()).fileName(file.getOriginalFilename())
                .url("/files/" + file.getOriginalFilename()).size(file.getSize()).build();

        byte[] cont = file.getBytes();
        assertThat(file.getSize() - cont.length, is(0L));

        fileStore.add(fm);
        return fm;
    }

    @Getter
    @Builder
    private static class FileModel {
        String id;
        String fileName;
        String url;
        long size;
    }

}

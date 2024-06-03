package net.n2oapp.framework.autotest.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Контроллер файлового хранилища для автотестов
 */
@Getter
@RestController
public class FileStoreController {

    private final List<FileModel> fileStore = Collections.synchronizedList(new ArrayList<>());

    public void clearFileStore() {
        fileStore.clear();
    }

    @PostMapping("/files")
    public ResponseEntity<FileModel> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(storeFile(file), HttpStatus.OK);
    }

    @PostMapping("/multi")
    public ResponseEntity<List<FileModel>> uploadMultiFile(@RequestParam("file") MultipartFile[] file) throws IOException {
        List<FileModel> result = new ArrayList<>();
        for (MultipartFile mf : file) {
            result.add(storeFile(mf));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/files/{id}")
    public void deleteFile(@PathVariable("id") String id) {
        for (FileModel fs : fileStore) {
            if (id.equals(fs.id)) {
                fileStore.remove(fs);
                break;
            }
        }
    }

    public ListResponse getList() {
        return new ListResponse(fileStore);
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
    @AllArgsConstructor
    static class ListResponse {
        @JsonProperty
        final Collection<FileModel> files;
    }

    @Getter
    @Builder
    public static class FileModel {
        String id;
        String fileName;
        String url;
        long size;
    }

}

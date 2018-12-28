package net.n2oapp.framework.examples.fileupload.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileModel {
    private final String id;
    private final String fileName;
    private final String url;
    private boolean stored;

    public FileModel(String id, String fileName, String url) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        stored = false;
    }

}

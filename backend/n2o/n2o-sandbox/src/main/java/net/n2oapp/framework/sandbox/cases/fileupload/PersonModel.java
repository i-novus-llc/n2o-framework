package net.n2oapp.framework.sandbox.cases.fileupload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class PersonModel implements Serializable {
    private String id;
    private String surname;
    private String name;
    private String patronymic;
    private HashMap<String, FileModel> files;

    public Collection<FileModel> getDocs() {
        return files == null ? null : files.values();
    }
}

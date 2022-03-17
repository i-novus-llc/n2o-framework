package net.n2oapp.framework.sandbox.server.fileupload;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonModel implements Serializable {
    @JsonProperty
    private String id;
    @JsonProperty
    private String surname;
    @JsonProperty
    private String name;
    @JsonProperty
    private String patronymic;
    private HashMap<String, FileModel> files;

    @JsonAnyGetter
    public Collection<FileModel> getDocs() {
        return files == null ? null : files.values();
    }
}

package net.n2oapp.framework.sandbox.server.editor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
public class FileModel {
    @JsonProperty
    private String file;
    @JsonProperty
    private String source;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileModel fileModel = (FileModel) o;
        return Objects.equals(file, fileModel.file) && Objects.equals(source, fileModel.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, source);
    }
}

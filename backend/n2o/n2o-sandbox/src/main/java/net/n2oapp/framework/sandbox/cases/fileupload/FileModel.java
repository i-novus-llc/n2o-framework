package net.n2oapp.framework.sandbox.cases.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileModel implements Serializable {
    @JsonProperty
    private String id;
    @JsonProperty
    private String fileName;
    @JsonProperty
    private String url;
    @JsonProperty
    private long size;
    @JsonProperty
    private LocalDateTime date;
}

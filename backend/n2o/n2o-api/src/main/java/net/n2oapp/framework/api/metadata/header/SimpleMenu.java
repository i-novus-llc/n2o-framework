package net.n2oapp.framework.api.metadata.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SimpleMenu implements Compiled {
    @JsonProperty
    private String src;
    @JsonProperty
    private List<HeaderItem> items;

}

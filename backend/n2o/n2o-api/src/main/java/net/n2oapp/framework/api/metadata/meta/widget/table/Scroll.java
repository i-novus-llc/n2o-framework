package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

@Getter
@Setter
public class Scroll implements Compiled {
    @JsonProperty
    private String x;
    @JsonProperty
    private String y;
}

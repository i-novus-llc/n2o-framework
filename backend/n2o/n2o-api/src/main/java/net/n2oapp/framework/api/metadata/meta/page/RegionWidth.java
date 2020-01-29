package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RegionWidth implements Serializable {

    public RegionWidth(String left, String right) {
        this.left = left;
        this.right = right;
    }

    @JsonProperty
    private String left;
    @JsonProperty
    private String right;
}


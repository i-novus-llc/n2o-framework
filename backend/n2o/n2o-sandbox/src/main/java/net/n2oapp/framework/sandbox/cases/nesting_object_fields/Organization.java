package net.n2oapp.framework.sandbox.cases.nesting_object_fields;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @JsonProperty
    private String code;
    @JsonProperty
    private String title;
}

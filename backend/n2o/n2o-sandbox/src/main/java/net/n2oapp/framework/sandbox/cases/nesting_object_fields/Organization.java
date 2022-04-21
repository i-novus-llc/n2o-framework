package net.n2oapp.framework.sandbox.cases.nesting_object_fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    private String code;
    private String title;
}
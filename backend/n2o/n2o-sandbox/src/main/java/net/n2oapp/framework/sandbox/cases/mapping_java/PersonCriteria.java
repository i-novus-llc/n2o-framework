package net.n2oapp.framework.sandbox.cases.mapping_java;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.Criteria;

@Getter
@Setter
public class PersonCriteria extends Criteria {

    private String firstName;

    public PersonCriteria() {
    }
}

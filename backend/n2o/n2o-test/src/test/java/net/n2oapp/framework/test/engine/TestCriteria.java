package net.n2oapp.framework.test.engine;

import net.n2oapp.criteria.api.Criteria;

public class TestCriteria extends Criteria {
    private Integer id;
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

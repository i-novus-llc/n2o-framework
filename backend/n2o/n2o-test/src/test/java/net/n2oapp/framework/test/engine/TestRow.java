package net.n2oapp.framework.test.engine;

import java.util.Objects;

public class TestRow {
    private Integer id;
    private String value;

    public TestRow(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRow testRow = (TestRow) o;
        return Objects.equals(id, testRow.id) &&
                Objects.equals(value, testRow.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, value);
    }
}

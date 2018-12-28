package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.Objects;


/**
 * Ссылка на модель Redux
 */
@Getter
public class BindLink implements Compiled {
    @JsonProperty("link")
    private String bindLink;
    @JsonProperty
    private Object value;

    public BindLink() {}

    public BindLink(String bindLink) {
        this.bindLink = bindLink;
    }

    public BindLink(String bindLink, Object value) {
        this.bindLink = bindLink;
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public void setBindLink(String bindLink) {
        this.bindLink = bindLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BindLink)) return false;
        BindLink bindLink1 = (BindLink) o;
        return Objects.equals(bindLink, bindLink1.bindLink) &&
                Objects.equals(value, bindLink1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindLink, value);
    }

    @Override
    public String toString() {
        return "BindLink{" +
                "bindLink='" + bindLink + '\'' +
                ", value=" + value +
                '}';
    }
}

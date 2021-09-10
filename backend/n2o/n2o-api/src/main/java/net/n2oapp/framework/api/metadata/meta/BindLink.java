package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;

import java.util.Objects;


/**
 * Ссылка на модель Redux
 */
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

    public String getBindLink() {
        return bindLink;
    }

    public Object getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isConst() {
        return !isEmpty() && !StringUtils.isJs(getValue()) || bindLink == null;
    }

    public boolean isLink() {
        return getBindLink() != null || StringUtils.isJs(getValue());
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Установить значение как ссылку на поле модели
     * @param field Поле модели
     */
    public void setFieldValue(String field) {
        this.value = Placeholders.js(field);
    }


    /**
     * Эквивалентны ли ссылки на модели без учёта значений и полей.
     * @param o Ссылка
     * @return true - эквивалентны, false - нет
     */
    public boolean equalsLink(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BindLink))
            return false;
        BindLink link = (BindLink) o;
        return Objects.equals(getBindLink(), link.getBindLink());
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

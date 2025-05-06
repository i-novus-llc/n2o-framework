package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;

import java.util.Objects;


/**
 * Ссылка на модель Redux
 */
public class BindLink implements Compiled {
    /**
     * Адрес ссылки в Redux
     */
    @JsonProperty
    private String link;
    /**
     * Константное значение или ссылка на поле в модели bindLink
     */
    @JsonProperty
    private Object value;

    public BindLink() {
    }

    public BindLink(String link) {
        this.link = link;
    }

    public BindLink(String link, Object value) {
        this.link = link;
        this.value = value;
    }

    public String getLink() {
        return link;
    }

    public String normalizeLink() {
        if (StringUtils.isJs(getValue()))
            return getLink() + "." + StringUtils.unwrapJs(value.toString());
        return getLink();
    }

    public Object getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isConst() {
        return !isEmpty() && !StringUtils.isJs(getValue()) || link == null;
    }

    public boolean isLink() {
        return getLink() != null || StringUtils.isJs(getValue());
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Установить значение как ссылку на поле модели
     *
     * @param field Поле модели
     */
    public void setFieldValue(String field) {
        this.value = Placeholders.js(field);
    }

    /**
     * Эквивалентны ли ссылки приведенные к строке
     *
     * @param o Ссылка
     * @return true - эквивалентны, false - нет
     */
    public boolean equalsNormalizedLink(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BindLink link))
            return false;
        if (getLink() == null || link.getLink() == null)
            return false;
        return Objects.equals(normalizeLink(), link.normalizeLink());
    }

    /**
     * Эквивалентны ли ссылки на модели без учёта значений и полей.
     *
     * @param o Ссылка
     * @return true - эквивалентны, false - нет
     */
    public boolean equalsLink(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BindLink link))
            return false;
        return Objects.equals(getLink(), link.getLink());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BindLink bindLink1)) return false;
        return Objects.equals(link, bindLink1.link) &&
                Objects.equals(value, bindLink1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, value);
    }

    @Override
    public String toString() {
        return "BindLink{" +
                "bindLink='" + link + '\'' +
                ", value=" + value +
                '}';
    }
}

package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Аргумент метода
 */
@Getter
@Setter
public class Argument implements Source {
    private String name;
    private String className;
    private Type type;
    private String defaultValue;

    /**
     * Contains information about type of argument
     */
    public enum Type implements IdAware {
        PRIMITIVE("primitive"),
        CLASS("class"),
        ENTITY("entity"),
        CRITERIA("criteria");

        private String xmlName;

        Type(String xmlName) {
            this.xmlName = xmlName;
        }

        @Override
        @JsonValue
        public String getId() {
            return this.xmlName;
        }

        @Override
        public void setId(String id) {
            throw new UnsupportedOperationException();
        }
    }
}
package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;

import java.io.Serializable;

/**
 * Аргумент метода
 */
public class Argument implements Serializable {
    private String name;
    private String className;
    private Type type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

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
        public String getId() {
            return xmlName;
        }
    }
}
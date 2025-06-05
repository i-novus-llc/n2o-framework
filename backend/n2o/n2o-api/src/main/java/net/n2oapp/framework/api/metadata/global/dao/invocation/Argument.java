package net.n2oapp.framework.api.metadata.global.dao.invocation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Аргумент метода
 */
@Getter
@Setter
public class Argument implements Source {
    private String name;
    private String className;
    private TypeEnum type;
    private String defaultValue;

    /**
     * Contains information about type of argument
     */
    @RequiredArgsConstructor
    @Getter
    public enum TypeEnum implements N2oEnum {
        PRIMITIVE("primitive"),
        CLASS("class"),
        ENTITY("entity"),
        CRITERIA("criteria");

        private final String id;
    }
}
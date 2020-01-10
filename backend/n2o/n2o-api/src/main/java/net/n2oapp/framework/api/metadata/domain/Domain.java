package net.n2oapp.framework.api.metadata.domain;

import net.n2oapp.criteria.dataset.DataSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Типы данных N2O
 */
public enum Domain {
    STRING("string", String.class),
    INTEGER("integer", Integer.class),
    NUMERIC("numeric", BigDecimal.class),
    LONG("long", Long.class),
    BYTE("byte", Byte.class),
    SHORT("short", Short.class),
    BOOLEAN("boolean", Boolean.class),
    DATE("date", Date.class, "YYYY-MM-DDTHH:mm:ss"),
    LOCALDATE("localdate", LocalDate.class, "YYYY-MM-DD"),
    LOCALDATETIME("localdatetime", LocalDateTime.class, "YYYY-MM-DDTHH:mm:ss"),
    ZONEDDATETIME("zoneddatetime", ZonedDateTime.class, "YYYY-MM-DDTHH:mm:ssZ"),
    OFFSETDATETIME("offsetdatetime", OffsetDateTime.class, "YYYY-MM-DDTHH:mm:ssZ"),
    OBJECT("object", DataSet.class);

    private String name;
    private Class type;
    private String jsFormat;

    Domain(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    Domain(String name, Class type, String jsFormat) {
        this.name = name;
        this.type = type;
        this.jsFormat = jsFormat;
    }

    public Class getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getJsFormat() {
        return jsFormat;
    }

    public String getArray() {
        return getName() + "[]";
    }

    public static Domain getByName(String name) {
        for (Domain domain : values()) {
            if (domain.getName().equalsIgnoreCase(name)) {
                return domain;
            }
        }
        return null;
    }

    public static Domain getByClass(Class<?> clazz) {
        for (Domain domain : values()) {
            if (domain.getType().equals(clazz)) {
                return domain;
            }
        }
        return null;
    }
}
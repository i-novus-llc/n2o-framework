package net.n2oapp.framework.api.metadata.domain;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.control.plain.N2oCheckbox;
import net.n2oapp.framework.api.metadata.control.list.N2oClassifier;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Типы данных N2O
 */
public enum Domain {
    integer(Integer.class, N2oInputText.class),
    string(String.class, N2oInputText.class),
    numeric(BigDecimal.class, N2oInputText.class),
    bool("boolean", Boolean.class, N2oCheckbox.class),
    date(Date.class, N2oDatePicker.class),
    localdate(LocalDate.class, N2oDatePicker.class),
    localdatetime(LocalDateTime.class, N2oDatePicker.class),
    object(DataSet.class, N2oClassifier.class),
    long_(Long.class, N2oInputText.class) {
        @Override
        public String getName() {
            return "long";
        }
    },
    byte_(Byte.class, N2oInputText.class) {
        @Override
        public String getName() {
            return "byte";
        }
    },
    short_(Short.class, N2oInputText.class) {
        @Override
        public String getName() {
            return "short";
        }
    };

    private String name;
    private Class typeClass;
    private Class<? extends N2oField> controlClass;

    private Domain(Class typeClass,
                   Class<? extends N2oField> controlClass) {
        this.name = name();
        this.typeClass = typeClass;
        this.controlClass = controlClass;
    }

    private Domain(String name, Class typeClass,
                   Class<? extends N2oField> controlClass) {
        this.name = name;
        this.typeClass = typeClass;
        this.controlClass = controlClass;
    }

    public Class getTypeClass() {
        return typeClass;
    }

    public Class<? extends N2oField> getControlClass() {
        return controlClass;
    }

    public String getName() {
        return name;
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

    public static Domain getByClass(Class clazz) {
        for (Domain domain : values()) {
            if (domain.getTypeClass().equals(clazz)) {
                return domain;
            }
        }
        return null;
    }
}
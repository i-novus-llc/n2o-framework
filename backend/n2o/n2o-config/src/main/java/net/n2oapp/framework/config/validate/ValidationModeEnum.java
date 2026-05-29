package net.n2oapp.framework.config.validate;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ValidationModeEnum {
    ON("on"),
    IGNORE_REFS("ignore-refs"),
    OFF("off");

    private final String value;

    ValidationModeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ValidationModeEnum of(String value) {
        for (ValidationModeEnum mode : values()) {
            if (mode.value.equals(value)) {
                return mode;
            }
        }
        String allowedValues = Arrays.stream(values())
                .map(m -> m.value)
                .collect(Collectors.joining(" | ", "[", "]"));
        throw new IllegalStateException(String.format(
                "Недопустимое значение настройки 'n2o.validation.mode'='%s'. Допустимые значения: %s",
                value, allowedValues));
    }
}

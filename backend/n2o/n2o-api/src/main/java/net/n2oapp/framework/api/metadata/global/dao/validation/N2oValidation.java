package net.n2oapp.framework.api.metadata.global.dao.validation;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Исходная модель абстрактной валидации
 */
@Getter
@Setter
public abstract class N2oValidation implements IdAware, Source, NamespaceUriAware {
    private String id;
    private String fieldId;
    private SeverityTypeEnum severity;
    private ServerMomentEnum serverMoment;
    private String message;
    private String title;
    private String namespaceUri;
    private String enabled;
    private String side;

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getNamespaceUri() {
        return "";
    }

    @RequiredArgsConstructor
    @Getter
    public enum ServerMomentEnum implements N2oEnum {
        BEFORE_OPERATION("before-operation"),
        BEFORE_QUERY("before-query"),
        AFTER_SUCCESS_QUERY("after-success-query"),
        AFTER_FAIL_QUERY("after-fail-query"),
        AFTER_FAIL_OPERATION("after-fail-operation"),
        AFTER_SUCCESS_OPERATION("after-success-operation");

        private final String id;
    }
}

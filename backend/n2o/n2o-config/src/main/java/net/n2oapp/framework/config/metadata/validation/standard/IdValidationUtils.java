package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.core.env.PropertyResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Утилиты проверки содержания в id зарезервированных слов
 */
public class IdValidationUtils {

    private final Set<String> forbiddenId;

    public IdValidationUtils(PropertyResolver propertyResolver) {
        String prop = propertyResolver.getProperty("n2o.config.field.forbidden_ids");
        forbiddenId = new HashSet<>();
        if (prop != null) {
            for (String item : prop.split(",")) {
                forbiddenId.add(item.trim());
            }
        }
    }

    public void checkId(IdAware idAware) {
        String id = idAware.getId();
        if (id != null && forbiddenId != null && forbiddenId.contains(id.trim())) {
            throw new N2oMetadataValidationException("Field ID contains forbidden word: \'" + id + "\'");
        }
    }

    public void checkIds(NamespaceUriAware[] items) {
        if (items != null && forbiddenId != null) {
            for (NamespaceUriAware item : items) {
                if (item instanceof IdAware) {
                    checkId((IdAware) item);
                }
            }
        }
    }
}

package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.core.env.PropertyResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Утилиты проверки содержания в id зарезервированных слов
 */
public final class IdValidationUtils {

    private IdValidationUtils() {
    }

    public static void checkIds(NamespaceUriAware[] items, ValidateProcessor p) {
        if (items != null) {
            for (NamespaceUriAware item : items) {
                if (item instanceof IdAware) {
                    p.checkId((IdAware) item, "Идентификатор поля {0} является запрещенным именем");
                }
            }
        }
    }

}

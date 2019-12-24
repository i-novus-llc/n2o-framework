package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;

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

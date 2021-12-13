package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;

/**
 * Утилиты проверки метаданных
 */
public final class IdValidationUtils {

    private IdValidationUtils() {
    }

    public static void checkIds(NamespaceUriAware[] items, SourceProcessor p) {
        if (items != null) {
            for (NamespaceUriAware item : items) {
                if (item instanceof IdAware) {
                    p.checkId((IdAware) item, "Идентификатор поля {0} является запрещенным именем");
                }
            }
        }
    }
}

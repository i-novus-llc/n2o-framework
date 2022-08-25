package net.n2oapp.framework.api.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Поставщик динамических метаданных
 */
public interface DynamicMetadataProvider {

    /**
     * Уникальный код поставщика
     */
    String getCode();

    /**
     * Получение метаданых по контексту
     * @param context контекст
     * @return список полученных метаданных
     */
    <T extends SourceMetadata> List<T> read(String context);

    default <T extends SourceMetadata> T read(String context, Class<T> metadataClass) {
        List<T> list = read(context);
        Optional<T> metadata = list.stream().filter(t -> t.getClass().isAssignableFrom(metadataClass)).findFirst();
        return metadata.orElse(null);
    }

    default Collection<Class<? extends SourceMetadata>> getMetadataClasses() {
        return Arrays.asList(N2oObject.class, N2oQuery.class, N2oPage.class, N2oWidget.class, N2oFieldSet.class);
    }

    /**
     * Кэшировать ли результаты провайдера
     * @return true кэшировать
     */
    default boolean cache(String params) {
        return false;
    }
}

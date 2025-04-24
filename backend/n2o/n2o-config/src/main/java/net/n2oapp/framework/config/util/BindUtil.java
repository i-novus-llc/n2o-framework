package net.n2oapp.framework.config.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

/**
 * Утилита для связывания метаданных с данными
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BindUtil {
    /**
     * Разрешение ссылок на предыдущие страницы в {@link ClientDataProvider}
     *
     * @param dataProvider Провайдер данных
     * @param p            Процессор
     */
    public static void bindDataProvider(ClientDataProvider dataProvider, BindProcessor p) {
        if (dataProvider == null)
            return;
        Map<String, ModelLink> pathMapping = dataProvider.getPathMapping();
        Map<String, ModelLink> queryMapping = dataProvider.getQueryMapping();
        dataProvider.setUrl(p.resolveUrl(dataProvider.getUrl(), pathMapping, queryMapping));
        if (pathMapping != null) {
            pathMapping.forEach((k, v) -> pathMapping.put(k, (ModelLink) p.resolveLink(v)));
        }
        if (queryMapping != null) {
            queryMapping.forEach((k, v) -> queryMapping.put(k, (ModelLink) p.resolveLink(v)));
        }
    }

    /**
     * Заменить в атрибутах расширения плейсхолдеры на значения
     *
     * @param metadata Компонент
     * @param p        Процессор
     */
    public static void resolveExtension(PropertiesAware metadata, BindProcessor p) {
        if (metadata.getProperties() != null)
            metadata.getProperties().entrySet().forEach(e -> {
                if (e.getValue() instanceof String str)
                    e.setValue(p.resolveText(str));
            });
    }
}

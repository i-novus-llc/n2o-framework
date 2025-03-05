package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

/**
 * Утилита для связывания метаданных с данными
 */
public class BindUtil {
    /**
     * Разрешение ссылок на предыдущие страницы в {@link ClientDataProvider}
     * @param dataProvider Провайдер данных
     * @param p Процессор
     */
    public static void bindDataProvider(ClientDataProvider dataProvider, BindProcessor p) {
        if (dataProvider == null)
            return;
        Map<String, ModelLink> pathMapping = dataProvider.getPathMapping();
        Map<String, ModelLink> queryMapping = dataProvider.getQueryMapping();
        dataProvider.setUrl(p.resolveUrl(dataProvider.getUrl(), pathMapping, queryMapping));
        if (pathMapping != null) {
            pathMapping.forEach((k, v) -> {
                if (v.getValue() instanceof String)
                    v.setValue(p.resolveText(v.getValue().toString()));
                pathMapping.put(k, (ModelLink) p.resolveLink(v));
            });
        }
        if (queryMapping != null) {
            queryMapping.forEach((k, v) -> {
                if (v.getValue() instanceof String)
                    v.setValue(p.resolveText(v.getValue().toString()));
                queryMapping.put(k, (ModelLink) p.resolveLink(v));
            });
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
                if (e.getValue() instanceof String)
                    e.setValue(p.resolveText(e.getValue().toString()));
            });
    }
}

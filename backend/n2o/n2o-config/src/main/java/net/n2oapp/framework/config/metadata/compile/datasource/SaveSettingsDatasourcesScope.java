package net.n2oapp.framework.config.metadata.compile.datasource;

import java.util.HashSet;
import java.util.Set;

/**
 * Скоуп для хранения идентификаторов датасурсов, для которых необходимо сохранять настройки.
 */
public class SaveSettingsDatasourcesScope {
    private final Set<String> saveSettingsDatasources = new HashSet<>();

    public SaveSettingsDatasourcesScope() {
    }

    public void markAsSaveSettingsEnabled(String datasourceId) {
        saveSettingsDatasources.add(datasourceId);
    }

    public boolean isSaveSettingsEnabled(String datasourceId) {
        return saveSettingsDatasources.contains(datasourceId);
    }
}
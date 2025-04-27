package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.tablesettings.*;

/**
 * Набор настроек таблицы
 */
public class N2oTableSettingsIOPack implements MetadataPack<XmlIOBuilder<?>> {
    @Override
    public void build(XmlIOBuilder<?> b) {
        b.ios(new FiltersTableSettingIOv1(),
                new RefreshTableSettingIOv1(),
                new ResizeTableSettingIOv1(),
                new WordWrapTableSettingIOv1(),
                new ExportTableSettingIOv1(),
                new ColumnsTableSettingIOv1(),
                new ResetTableSettingIOv1()
        );
    }
}

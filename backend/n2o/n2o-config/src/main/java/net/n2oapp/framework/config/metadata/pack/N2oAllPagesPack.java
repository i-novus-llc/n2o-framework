package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;

/**
 * Набор для компиляции страниц и всех вложенных метаданных
 */
public class N2oAllPagesPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oActionsPack(), new N2oCellsPack(),
                new N2oControlsPack(), new N2oChartsPack(), new N2oDialogsPack());
    }
}

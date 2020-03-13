package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.RatingCellElementIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование маппинга java модели в json для ячейки рейтинга
 */
public class RatingCellJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new RatingCellElementIOv2());
        builder.compilers(new RatingCellCompiler());
    }

    @Test
    public void ratingCell() {
        check("net/n2oapp/framework/config/mapping/testRatingCell.widget.xml",
                "components/widgets/Table/cells/RatingCell/RatingCell.meta.json")
                .cutXml("table.cells[0]")
                .exclude("fieldKey", "src")
                .assertEquals();
    }
}
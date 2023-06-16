package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.cell.ImageCell;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.ImageCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции ячейки с изображением
 */
public class ImageCellCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.ios(new ImageCellElementIOv2());
        builder.compilers(new ImageCellCompiler());
    }

    @Test
    void testImageCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testImageCell.page.xml")
                .get(new PageContext("testImageCell"));
        Table table = (Table) page.getWidget();
        ImageCell cell = (ImageCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("ImageCell"));
        assertThat(cell.getShape(), is(ShapeType.ROUNDED));
        assertThat(cell.getWidth(), is("20px"));
        assertThat(cell.getProperties().size(), is(1));
        assertThat(cell.getTitle(), is("`Title`"));
        assertThat(cell.getDescription(), is("`Description`"));
        assertThat(cell.getData(), is("`'data:image/jpeg;base64'+image`"));
        assertThat(cell.getTextPosition(), is(N2oImageCell.Position.left));
        assertThat(cell.getStatuses().length, is(2));
        assertThat(cell.getStatuses()[0].getSrc(), is("testSrc"));
        assertThat(cell.getStatuses()[0].getFieldId(), is("status1"));
        assertThat(cell.getStatuses()[0].getIcon(), is("`icon1`"));
        assertThat(cell.getStatuses()[0].getPlace(), is(ImageStatusElementPlace.topRight));
        assertThat(cell.getStatuses()[1].getFieldId(), is("id"));
        assertThat(cell.getStatuses()[1].getSrc(), is("Status"));
        assertThat(cell.getStatuses()[1].getIcon(), is(nullValue()));
        assertThat(cell.getStatuses()[1].getPlace(), is(ImageStatusElementPlace.topLeft));

        assertThat(cell.getAction(), instanceOf(LinkActionImpl.class));

        cell = (ImageCell) table.getComponent().getCells().get(1);
        assertThat(cell.getWidth(), nullValue());
        assertThat(cell.getTextPosition(), is(N2oImageCell.Position.right));
        assertThat(cell.getShape(), is(ShapeType.SQUARE));
    }
}

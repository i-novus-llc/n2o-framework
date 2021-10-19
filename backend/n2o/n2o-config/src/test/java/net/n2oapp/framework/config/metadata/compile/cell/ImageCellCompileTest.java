package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.ImageCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции ячейки с изображением
 */
public class ImageCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
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
    public void testImageCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testImageCell.widget.xml")
                .get(new WidgetContext("testImageCell"));

        N2oImageCell cell = (N2oImageCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("ImageCell"));
        assertThat(cell.getShape(), is(ImageShape.rounded));
        assertThat(cell.getWidth(), is("20"));
        assertThat(cell.getProperties().size(), is(1));
        assertThat(cell.getTitle(), is("`Title`"));
        assertThat(cell.getDescription(), is("`Description`"));
        assertThat(cell.getData(), is("`'data:image/jpeg;base64'+image`"));
        assertThat(cell.getTextPosition(), is(N2oImageCell.Position.left));
        assertThat(cell.getStatuses().length, is(2));
        assertThat(cell.getStatuses()[0].getSrc(), is("testSrc"));
        assertThat(cell.getStatuses()[0].getFieldId(), is("status1"));
        assertThat(cell.getStatuses()[0].getIcon(), is("`icon1`"));
        assertThat(cell.getStatuses()[0].getPlace(), is(ImageStatusElement.Place.topRight));
        assertThat(cell.getStatuses()[1].getFieldId(), is("id"));
        assertThat(cell.getStatuses()[1].getSrc(), is("Status"));
        assertThat(cell.getStatuses()[1].getIcon(), is(nullValue()));
        assertThat(cell.getStatuses()[1].getPlace(), is(ImageStatusElement.Place.topLeft));

        assertThat(cell.getCompiledAction(), instanceOf(LinkActionImpl.class));

        cell = (N2oImageCell) table.getComponent().getCells().get(1);
        assertThat(cell.getWidth(), nullValue());
        assertThat(cell.getTextPosition(), is(N2oImageCell.Position.right));
        assertThat(cell.getShape(), is(ImageShape.square));
    }
}

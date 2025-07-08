package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.meta.cell.FileUploadCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.FileUploadCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции ячейки загрузки файлов
 */
class FileUploadCellCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack());
        builder.ios(new FileUploadCellElementIOv2());
        builder.compilers(new FileUploadCellCompiler());
    }

    @Test
    void testCompileFileUploadCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testFileUploadCell.page.xml")
                .get(new PageContext("testFileUploadCell"));
        Table<?> table = (Table<?>) page.getWidget();
        FileUploadCell cell = (FileUploadCell) table.getComponent().getBody().getCells().get(0);
        checkCell0(cell);

        cell = (FileUploadCell) table.getComponent().getBody().getCells().get(1);
        checkCell1(cell);
    }

    private static void checkCell0(FileUploadCell cell) {
        assertThat(cell.getSrc(), is("FileUploadCell"));
        assertThat(cell.getElementAttributes().get("className"), nullValue());
        assertThat(cell.getVisible(), nullValue());
        assertThat(cell.getVisible(), nullValue());
        assertThat(cell.getElementAttributes().get("style"), nullValue());
        assertThat(cell.getMulti(), is(false));
        assertThat(cell.getAjax(), is(true));
        assertThat(cell.getUploadUrl(), nullValue());
        assertThat(cell.getDeleteUrl(), nullValue());
        assertThat(cell.getValueFieldId(), is("id"));
        assertThat(cell.getLabelFieldId(), is("name"));
        assertThat(cell.getResponseFieldId(), is("message"));
        assertThat(cell.getUrlFieldId(), is("url"));
        assertThat(cell.getRequestParam(), is("file"));
        assertThat(cell.getShowSize(), is(true));
        assertThat(cell.getAccept(), nullValue());
        assertThat(cell.getLabel(), nullValue());
        assertThat(cell.getDeleteIcon(), nullValue());
        assertThat(cell.getUploadIcon(), nullValue());
        assertThat(cell.getUploadIcon(), nullValue());
    }

    private static void checkCell1(FileUploadCell cell) {
        assertThat(cell.getSrc(), is("src"));
        assertThat(cell.getElementAttributes().get("className"), is("testClass"));
        assertThat(cell.getVisible(), is(true));
        assertThat(cell.getElementAttributes().get("style"), notNullValue());
        assertThat(cell.getMulti(), is(true));
        assertThat(cell.getAjax(), is(false));
        assertThat(cell.getUploadUrl(), is("/uploadDoc"));
        assertThat(cell.getDeleteUrl(), is("/deleteDoc"));
        assertThat(cell.getValueFieldId(), is("testId"));
        assertThat(cell.getLabelFieldId(), is("testName"));
        assertThat(cell.getResponseFieldId(), is("testMessage"));
        assertThat(cell.getUrlFieldId(), is("/downloadDoc"));
        assertThat(cell.getRequestParam(), is("testFile"));
        assertThat(cell.getShowSize(), is(false));
        assertThat(cell.getAccept(), is("jpg,doc"));
        assertThat(cell.getLabel(), is("label"));
        assertThat(cell.getDeleteIcon(), is("deleteIcon"));
        assertThat(cell.getUploadIcon(), is("uploadIcon"));
        assertThat(cell.getUploadIcon(), is("uploadIcon"));
    }
}

package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;

/**
 * Тестирование компиляции кнопки с выпадающим меню
 */
public class SubMenuCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack(), new N2oPagesPack(), new N2oRegionsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }
//
//    @Test FIXME
//    public void testSubMenu() {
//        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testSubMenu.page.xml")
//                .get(new PageContext("testSubMenu"));
//
//        Submenu subMenu = (Submenu) ((Table)page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().getButton("mi0");
//        assertThat(subMenu.getSrc(), is("DropdownButton"));
//        assertThat(subMenu.getShowToggleIcon(), is(false));
//        assertThat(subMenu.getVisible().toString(), is("false"));
//
//        List<PerformButton> items = subMenu.getSubMenu();
//        assertThat(items.size(), is(2));
//        PerformButton createBtn = items.get(0);
//        assertThat(createBtn.getId(), is("create"));
//        assertThat(createBtn.getLabel(), is("Создать"));
//        assertThat(createBtn.getAction(), instanceOf(ShowModal.class));
//        PerformButton updateBtn = items.get(1);
//        assertThat(updateBtn.getId(), is("update"));
//        assertThat(updateBtn.getLabel(), is("Изменить"));
//        assertThat(updateBtn.getAction(), instanceOf(ShowModal.class));
//        assertThat(updateBtn.getConditions().get(ValidationType.enabled).size(), is(1));
//
//
//        subMenu = (Submenu) ((Table)page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().getButton("mi3");
//        assertThat(subMenu.getShowToggleIcon(), is(true));
//        assertThat(subMenu.getSubMenu(), nullValue());
//    }
}

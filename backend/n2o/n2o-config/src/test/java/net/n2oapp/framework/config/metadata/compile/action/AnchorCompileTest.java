package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AnchorCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                        new N2oActionsPack(), new N2oApplicationPack(), new N2oAllDataPack(),
                        new N2oControlsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testAnchorAction2.page.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testAnchorMenuItem.query.xml"));
    }

    @Test
    void testAnchor() {
        //Root page
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testAnchorAction.page.xml")
                .get(new PageContext("testAnchorAction"));
        Toolbar toolbar = ((Widget<?>) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar();
        LinkAction link1 = (LinkAction) toolbar.getButton("id1").getAction();
        assertThat(link1.getUrl(), is("/test"));
        assertThat(link1.getTarget(), is(TargetEnum.APPLICATION));
        assertThat(link1.getPathMapping().size(), is(0));
        assertThat(link1.getQueryMapping().size(), is(0));


        LinkActionImpl link2 = (LinkActionImpl) toolbar.getButton("id2").getAction();
        assertThat(link2.getUrl(), is("/test2/:param1/:param2?param3=:param3"));
        assertThat(link2.getTarget(), is(TargetEnum.APPLICATION));
        assertThat(link2.getPathMapping().size(), is(2));
        assertThat(link2.getPathMapping().get("param1").getLink(), is("models.filter['page_secondWgt']"));
        assertThat(link2.getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(link2.getPathMapping().get("param2").getLink(), is("models.resolve['page_test']"));
        assertThat(link2.getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(link2.getQueryMapping().size(), is(1));
        assertThat(link2.getQueryMapping().get("param3").getLink(), is("models.filter['page_secondWgt']"));
        assertThat(link2.getQueryMapping().get("param3").getValue(), is("`field3`"));

        LinkActionImpl link3 = (LinkActionImpl) toolbar.getButton("id3").getAction();
        assertThat(link3.getUrl(), is("http://google.com"));
        assertThat(link3.getTarget(), is(TargetEnum.SELF));

        LinkActionImpl linkSecond = (LinkActionImpl) ((Widget<?>) page.getRegions().get("single").get(1).getContent().get(0))
                .getToolbar().getButton("secWgt").getAction();

        assertThat(linkSecond.getUrl(), is("/test/:minPrice"));
        assertThat(linkSecond.getTarget(), is(TargetEnum.NEW_WINDOW));
        assertThat(linkSecond.getPathMapping().size(), is(1));
        assertThat(linkSecond.getPathMapping().get("minPrice").getLink(), is("models.filter['page_test']"));
        assertThat(linkSecond.getPathMapping().get("minPrice").getValue(), is("`minPrice`"));

        LinkActionImpl linkWithParam = (LinkActionImpl) ((Widget<?>) page.getRegions().get("single").get(1).getContent().get(0))
                .getToolbar().getButton("withParam").getAction();

        assertThat(linkWithParam.getUrl(), is("`url`"));

        //Modal page
        checkModalPage();
    }

    private void checkModalPage() {
        LinkActionImpl link2;
        LinkActionImpl link3;
        LinkAction link1;
        PageContext modalContext = (PageContext) route("/page/id4", Page.class);
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        link1 = (LinkActionImpl) modalPage.getWidget().getToolbar().getButton("id1").getAction();
        assertThat(link1.getUrl(), is("/test"));
        assertThat(link1.getTarget(), is(TargetEnum.APPLICATION));
        assertThat(link1.getPathMapping().size(), is(0));
        assertThat(link1.getQueryMapping().size(), is(0));

        link2 = (LinkActionImpl) modalPage.getWidget().getToolbar().getButton("id2").getAction();
        assertThat(link2.getUrl(), is("/test2/:param1/:param2?param3=:param3"));
        assertThat(link2.getTarget(), is(TargetEnum.APPLICATION));
        assertThat(link2.getPathMapping().size(), is(2));
        assertThat(link2.getPathMapping().get("param1").getLink(), is("models.resolve['page_id4_test']"));
        assertThat(link2.getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(link2.getPathMapping().get("param2").getLink(), is("models.resolve['page_id4_test']"));
        assertThat(link2.getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(link2.getQueryMapping().size(), is(1));
        assertThat(link2.getQueryMapping().get("param3").getLink(), is("models.resolve['page_id4_test']"));
        assertThat(link2.getQueryMapping().get("param3").getValue(), is("`field3`"));

        link3 = (LinkActionImpl) modalPage.getWidget().getToolbar().getButton("id3").getAction();
        assertThat(link3.getUrl(), is("/test3"));
        assertThat(link3.getTarget(), is(TargetEnum.APPLICATION));
        assertThat(link3.getPathMapping().size(), is(0));
        assertThat(link3.getQueryMapping().size(), is(0));
    }

    @Test
    void testAnchorInMenuItem() {
        Application application = compile("net/n2oapp/framework/config/metadata/compile/action/testAnchorMenuItem.application.xml")
                .get(new ApplicationContext("testAnchorMenuItem"));

        MenuItem menuItem = application.getSidebars().get(0).getMenu().getItems().get(0);
        assertThat(menuItem.getSrc(), is("LinkMenuItem"));
        assertThat(menuItem.getHref(), is("/person/:id/docs"));
        assertThat(menuItem.getLinkType(), is(MenuItem.LinkTypeEnum.INNER));
        assertThat(menuItem.getPathMapping().size(), is(1));
        assertThat(menuItem.getPathMapping().get("id").getValue(), is(":id"));
        assertThat(menuItem.getQueryMapping().size(), is(1));
        assertThat(menuItem.getQueryMapping().get("name").getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(menuItem.getQueryMapping().get("name").getDatasource(), is("doc"));
        assertThat(menuItem.getQueryMapping().get("name").getLink(), is("models.resolve['doc']"));
        assertThat(menuItem.getQueryMapping().get("name").getValue(), is("`name`"));

        menuItem = application.getSidebars().get(0).getMenu().getItems().get(1);
        assertThat(menuItem.getSrc(), is("LinkMenuItem"));
        assertThat(menuItem.getHref(), is("/person/:id/profile"));
        assertThat(menuItem.getLinkType(), is(MenuItem.LinkTypeEnum.INNER));
        assertThat(menuItem.getPathMapping().size(), is(1));
        assertThat(menuItem.getPathMapping().get("id").getValue(), is(":id"));
        assertThat(menuItem.getQueryMapping().size(), is(1));
        assertThat(menuItem.getQueryMapping().get("name").getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(menuItem.getQueryMapping().get("name").getDatasource(), is("person"));
        assertThat(menuItem.getQueryMapping().get("name").getLink(), is("models.resolve['person']"));
        assertThat(menuItem.getQueryMapping().get("name").getValue(), is("`name`"));
    }

    @Test
    void testLinkedHref() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testLinkedHref.page.xml")
                .get(new PageContext("testLinkedHref"));

        Toolbar toolbar = ((Widget<?>) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar();
        LinkActionImpl link1 = (LinkActionImpl) toolbar.getButton("b1").getAction();
        assertThat(link1.getUrl(), is("`url`"));
        assertThat(link1.getPayload().getModelLink(), is("models.filter['testLinkedHref_ds1']"));

        toolbar = page.getToolbar();
        LinkActionImpl link2 = (LinkActionImpl) toolbar.getButton("b2").getAction();
        assertThat(link2.getUrl(), is("`url`"));
        assertThat(link2.getPayload().getModelLink(), is("models.resolve['testLinkedHref_ds1']"));
    }
}

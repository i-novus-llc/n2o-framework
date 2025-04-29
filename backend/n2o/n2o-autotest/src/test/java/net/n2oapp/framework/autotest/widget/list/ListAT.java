package net.n2oapp.framework.autotest.widget.list;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для виджета Список
 */
class ListAT extends AutoTestBase {
    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testList() {
        setResourcePath("net/n2oapp/framework/autotest/widget/list");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/list/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/list/form.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/list/testList.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        ListWidget listWidget = page.widget(ListWidget.class);
        listWidget.shouldHaveSize(10);
        listWidget.content(0).body(TextCell.class).shouldHaveText("body1");
        listWidget.content(0).leftTop(ImageCell.class).shouldHaveSrc(getBaseUrl() + "/favicon.ico");
        listWidget.content(0).leftBottom(TextCell.class).shouldHaveText("leftBottom1");
        listWidget.content(0).subHeader(BadgeCell.class).shouldHaveColor(ColorsEnum.SUCCESS);
        listWidget.paging().shouldHaveTotalElements(11);
        listWidget.paging().selectNext();
        listWidget.paging().shouldHaveActivePage("2");
        listWidget.shouldHaveSize(1);
        listWidget.paging().selectPrev();
        listWidget.paging().shouldHaveActivePage("1");
        listWidget.shouldHaveSize(10);

        listWidget.content(0).click();
        SimplePage openPage = N2oSelenide.page(SimplePage.class);
        openPage.shouldExists();
        FormWidget form = openPage.widget(FormWidget.class);
        form.shouldExists();
        form.fields().field("body").control(InputText.class).shouldHaveValue("body1");
    }
}

package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.SubPageRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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
 * Автотест для региона подстраниц
 */
class SubPageRegionAT extends AutoTestBase {
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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());

    }

    @Test
    void testSubPageWithButtons() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/with_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/audio.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/friends.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/user.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/userInfo.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/video.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().row(1).click();
        table.toolbar().topRight().button("open-page").click();

        StandardPage userPage = N2oSelenide.page(StandardPage.class);
        userPage.breadcrumb().crumb(1).shouldHaveLabel("Страница пользователя");
        FormWidget form = userPage.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        Toolbar toolbar = form.toolbar().bottomLeft();

        SimplePage subPage = userPage.regions().region(1, SubPageRegion.class).content(SimplePage.class);
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2");
        form = subPage.widget(FormWidget.class);
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("userInfo sub-page");

        toolbar.button("Друзья").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/friends");
        form.fields().field("friends").control(OutputText.class).shouldHaveValue("FRIENDS");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("friends sub-page");

        toolbar.button("Инфо").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/info");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("info sub-page");

        toolbar.button("Аудиозаписи").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/audio");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("audio sub-page");

        toolbar.button("Видеозаписи").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/video");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("video sub-page");

        userPage.breadcrumb().crumb(1).click();
        userPage.breadcrumb().crumb(2).shouldHaveLabel("userInfo sub-page");
    }

    @Test
    void testSubPageWithoutButtons() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/without_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/audio.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/friends.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/user.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/userInfo.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/video.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().row(2).click();
        table.toolbar().topRight().button("open-page").click();

        StandardPage userPage = N2oSelenide.page(StandardPage.class);
        userPage.breadcrumb().crumb(1).shouldHaveLabel("Страница пользователя");

        SimplePage subPage = userPage.regions().region(0, SubPageRegion.class).content(SimplePage.class);
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3");
        FormWidget form = subPage.widget(FormWidget.class);
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");

        Toolbar toolbar = form.toolbar().bottomLeft();
        toolbar.button("Вперед").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/friends");
        form.fields().field("friends").control(OutputText.class).shouldHaveValue("FRIENDS");

        toolbar.button("Назад").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/info");

        toolbar.button("Вперед").click();
        toolbar.button("Вперед").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/audio");

        toolbar.button("Вперед").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/video");
    }

    @Test
    void testSubPageInModal() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/modal");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/audio.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/friends.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/user.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/userInfo.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/video.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().row(1).click();
        table.toolbar().topRight().button("show-modal").click();

        Modal modal = N2oSelenide.modal();
        StandardPage userPage = modal.content(StandardPage.class);
        userPage.shouldExists();

        FormWidget form = userPage.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        Toolbar toolbar = form.toolbar().bottomLeft();

        SimplePage subPage = userPage.regions().region(1, SubPageRegion.class).content(SimplePage.class);
        form = subPage.widget(FormWidget.class);
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");

        toolbar.button("Друзья").click();
        form.fields().field("friends").control(OutputText.class).shouldHaveValue("FRIENDS");

        toolbar.button("Инфо").click();
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");

        toolbar.button("Аудиозаписи").click();
        form.fields().field("audio").control(OutputText.class).shouldHaveValue("AUDIO");

        toolbar.button("Видеозаписи").click();
        form.fields().field("video").control(OutputText.class).shouldHaveValue("VIDEO");
    }
}
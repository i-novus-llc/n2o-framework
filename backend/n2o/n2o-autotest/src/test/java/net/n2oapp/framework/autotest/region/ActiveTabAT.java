package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Автотест использования активных вкладок в модели данных
 */
public class ActiveTabAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack());
    }

    @Test
    public void testTabsRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/active/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        StandardButton pageButton = page.toolbar().bottomRight().button("Кнока на странице для второй вкладки");
        pageButton.shouldNotExists();
        FormWidget tabsDsForm = page.regions().region(2, SimpleRegion.class).content().widget(FormWidget.class);
        tabsDsForm.shouldExists();
        InputText nameControl = tabsDsForm.fields().field("name").control(InputText.class);
        nameControl.shouldHaveValue("Ivan");
        InputText activeTabControl = tabsDsForm.fields().field("activeTab").control(InputText.class);
        activeTabControl.shouldHaveValue("tab3");

        SimpleRegion region = page.regions().region(0, SimpleRegion.class);
        region.shouldExists();
        FormWidget formWidget = region.content().widget(FormWidget.class);
        formWidget.shouldExists();
        // проверяем что видна только одна вкладка и она активна
        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(1);
        tabs.tab(0).shouldHaveName("Третья");
        page.urlShouldMatches(".*tabs1=tab3");

        // проверяем что становится активна первая вклдака и кнопка в форме
        RadioGroup radioGroup = formWidget.fields().field("Вкладки").control(RadioGroup.class);
        radioGroup.check("Первая");
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("Первая");
        tabs.tab(0).click();
        page.urlShouldMatches(".*tabs1=tab1");
        nameControl.shouldHaveValue("Ivan");
        activeTabControl.shouldHaveValue("tab1");
        StandardButton formButton = tabsDsForm.toolbar().topLeft().button("Кнопка на форме для первой вкладки");
        formButton.shouldExists();
        // проверяем что становится активна вторая вклдака и кнопка на странице
        radioGroup.check("Вторая");
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("Вторая");
        tabs.tab(0).click();
        tabs.tab(0).shouldBeActive();
        page.urlShouldMatches(".*tabs1=tab2");
        nameControl.shouldHaveValue("Ivan");
        activeTabControl.shouldHaveValue("tab2");
        pageButton.shouldExists();
        // вводим в поле значение и проверяем, что вклдака переключится
        activeTabControl.val("tab3");
        tabs.tab(1).shouldBeActive();
    }
}

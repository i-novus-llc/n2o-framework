package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
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

import java.time.Duration;

/**
 * Автотест для проверки востановления значений фильтров таблицы после закрытия страницы
 */
public class RestoreFiltersAT extends AutoTestBase {

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    public void afterPageClosing() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_close");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_close/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_close/open.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_close/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        StandardPage secPage = N2oSelenide.page(StandardPage.class);
        Modal modal = N2oSelenide.modal();
        Drawer drawer = N2oSelenide.drawer();
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        Button search = table.filters().toolbar().button("Найти");
        InputText filter = table.filters().fields().field("name").control(InputText.class);
        Button openPage = table.toolbar().topLeft().button("open-page");
        Button showModal = table.toolbar().topLeft().button("show-modal");
        Button openDrawer = table.toolbar().topLeft().button("open-drawer");
        Button close = secPage.toolbar().bottomRight().button("Закрыть");

        table.shouldExists();
        table.columns().rows().shouldHaveSize(9);
        filter.setValue("test2");
        search.click();
        table.columns().rows().shouldHaveSize(1);

        openPage.click();
        secPage.shouldExists();
        close.click();
        page.shouldExists();
        filter.shouldHaveValue("test2");
        table.columns().rows().shouldHaveSize(1);

        openPage.click();
        secPage.shouldExists();
        secPage.breadcrumb().crumb("Восстановление фильтров после закрытия страницы").click();
        page.shouldExists();
        filter.shouldHaveValue("test2");
        table.columns().rows().shouldHaveSize(1);

        showModal.click();
        modal.shouldExists();
        modal.toolbar().bottomRight().button("Закрыть").click();
        modal.shouldNotExists();
        page.shouldExists();
        filter.shouldHaveValue("test2");
        table.columns().rows().shouldHaveSize(1);

        openDrawer.click();
        drawer.shouldExists();
        drawer.toolbar().bottomRight().button("Закрыть").click();
        drawer.shouldNotExists();
        page.shouldExists();
        filter.shouldHaveValue("test2");
        table.columns().rows().shouldHaveSize(1);

        Selenide.back();
        secPage.shouldExists();
        secPage.breadcrumb().shouldHaveSize(2);
        Selenide.forward();
        page.shouldExists();
        table.shouldExists();
        filter.shouldExists();
        filter.shouldHaveValue("test2", Duration.ofSeconds(15));
        table.columns().rows().shouldHaveSize(1);
    }

    @Test
    void afterPageReloading() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_page_reloading");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_page_reloading/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_page_reloading/kind.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_page_reloading/person.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/restore_filters_after_page_reloading/person.object.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.paging().shouldHaveTotalElements(4);

        InputSelect kindInpSelect = table.filters().fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("Вид документа").control(InputSelect.class);
        kindInpSelect.openPopup();
        kindInpSelect.dropdown().selectMulti(0, 2);
        kindInpSelect.shouldSelectedMultiSize(2);
        kindInpSelect.closePopup();
        table.filters().toolbar().button("Найти").click();
        table.paging().shouldHaveTotalElements(3);

        Selenide.refresh();

        page.shouldExists();
        table.paging().shouldHaveTotalElements(3);
        kindInpSelect.shouldSelectedMultiSize(2);
    }
}

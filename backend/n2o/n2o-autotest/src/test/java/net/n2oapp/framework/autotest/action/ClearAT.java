package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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
 * Автотест с проверками действия clear
 */

public class ClearAT extends AutoTestBase {

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

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/clear/index.page.xml"));
    }

    @Test
    public void clearAfterAction() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(FormWidget.class);
        form.shouldExists();

        InputText inputText = form.fields().field("Исходные данные").control(InputText.class);
        inputText.shouldHaveValue("Привет, Мир!");

        StandardButton clearBtn = form.toolbar().topLeft().button("clear");
        StandardButton copyBtn = form.toolbar().topLeft().button("copy");

        clearBtn.click();
        inputText.shouldBeEmpty();

        copyBtn.click();
        inputText.val("value");

        clearBtn.click();
        inputText.shouldBeEmpty();
    }
}

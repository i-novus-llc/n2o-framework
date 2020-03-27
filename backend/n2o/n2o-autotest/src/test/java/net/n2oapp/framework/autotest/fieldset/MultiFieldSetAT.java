package net.n2oapp.framework.autotest.fieldset;

import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест для филдсета с динамическим числом полей
 */
public class MultiFieldSetAT extends AutoTestBase {

    private SimplePage page;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testAdd() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/multiset/add/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        page = open(SimplePage.class);
        page.shouldExists();

        MultiFieldSet fieldset1 = page.single().widget(FormWidget.class).fieldsets().fieldset(0, MultiFieldSet.class);
        fieldset1.shouldExists();
        fieldset1.shouldBeEmpty();
        fieldset1.addButtonShouldNotBeExist();

        MultiFieldSet fieldset2 = page.single().widget(FormWidget.class).fieldsets().fieldset(1, MultiFieldSet.class);
        fieldset2.shouldExists();
        fieldset2.addButtonShouldBeExist();
        fieldset2.addButtonShouldHaveLabel("Добавить участника");
        fieldset2.clickAddButton();
        fieldset2.clickAddButton();
        fieldset2.shouldHaveItems(2);


    }

}

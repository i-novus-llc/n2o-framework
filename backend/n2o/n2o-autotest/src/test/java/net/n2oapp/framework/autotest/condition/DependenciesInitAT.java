package net.n2oapp.framework.autotest.condition;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.fieldset.LineFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест инициализации зависимостей
 */
public class DependenciesInitAT extends AutoTestBase {

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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/init/index.page.xml"));
    }

    @Test
    public void testApplyOnInit() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Инициализация зависимостей (apply-on-init)");

        FieldSets fieldsSets = page.widget(FormWidget.class).fieldsets();

        LineFieldSet initTrue = fieldsSets.fieldset(0, LineFieldSet.class);
        initTrue.fields().shouldHaveSize(3);
        initTrue.fields().field("Это поле невидимо при инициализации").shouldNotExists();
        initTrue.fields().field("Это поле заблокировано при инициализации").control(InputText.class).shouldBeDisabled();
        initTrue.fields().field("Это поле обязательно при инициализации").shouldBeRequired();
        initTrue.fields().field("Это поле заполнено при инициализации").control(InputText.class).shouldHaveValue("заполнено");

        LineFieldSet initFalse = fieldsSets.fieldset(1, LineFieldSet.class);
        initFalse.fields().shouldHaveSize(4);
        initFalse.fields().field("Это поле видимо при инициализации").shouldExists();
        initFalse.fields().field("Это поле разблокировано при инициализации").control(InputText.class).shouldBeEnabled();
        initFalse.fields().field("Это поле не обязательно при инициализации").shouldNotBeRequired();
        initFalse.fields().field("Это поле пустое при инициализации").control(InputText.class).shouldBeEmpty();
    }

}

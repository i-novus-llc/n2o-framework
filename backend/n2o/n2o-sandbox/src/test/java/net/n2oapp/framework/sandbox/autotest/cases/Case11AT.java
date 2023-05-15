package net.n2oapp.framework.sandbox.autotest.cases;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест фильтрации списковых полей
 */
public class Case11AT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.transformers(new TestEngineQueryTransformer());
        builder.sources(new CompileInfo("versions/7.0/case11/index.page.xml"),
                new CompileInfo("META-INF/conf/test.object.xml"),
                new CompileInfo("META-INF/conf/test.query.xml"));
    }

    @Test
    public void testListFieldFiltration() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();

        InputText type = fields.field("type").control(InputText.class);
        type.shouldHaveValue("1");

        InputSelect filteredByLink = fields.field("Фильтр по type").control(InputSelect.class);
        InputSelect filteredByConstant = fields.field("Фильтр по константе").control(InputSelect.class);
        InputSelect filteredByLinkWithCache = fields.field("Фильтр по ссылке с кэшированием").control(InputSelect.class);
        RadioGroup radioGroup = fields.field("Фильтр по радио кнопкам").control(RadioGroup.class);
        CheckboxGroup checkboxGroup = fields.field("Фильтр по чекбоксам").control(CheckboxGroup.class);

        filteredByLink.openPopup();
        filteredByLink.dropdown().shouldHaveOptions("test1", "test2");
        filteredByConstant.openPopup();
        filteredByConstant.dropdown().shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.openPopup();
        filteredByLinkWithCache.dropdown().shouldHaveOptions("test1", "test2");
        radioGroup.shouldHaveOptions("test1", "test2");
        checkboxGroup.shouldHaveOptions("test1", "test2");

        type.click();
        type.setValue("2");
        filteredByLink.openPopup();
        filteredByLink.dropdown().shouldHaveOptions("test3", "test4");
        filteredByConstant.openPopup();
        filteredByConstant.dropdown().shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.openPopup();
        filteredByLinkWithCache.dropdown().shouldHaveOptions("test3", "test4");
        radioGroup.shouldHaveOptions("test3", "test4");
        checkboxGroup.shouldHaveOptions("test3", "test4");
    }
}
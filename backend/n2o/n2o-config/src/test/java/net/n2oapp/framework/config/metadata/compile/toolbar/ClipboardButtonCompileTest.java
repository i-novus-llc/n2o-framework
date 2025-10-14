package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ClipboardButtonDataEnum;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ClipboardButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции кнопки {@code <clipboard-button>}
 */
class ClipboardButtonCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    void test() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testClipboardButton.page.xml")
                .get(new PageContext("testClipboardButton"));
        Form form = (Form) page.getRegions().get("single").getFirst().getContent().getFirst();

        // Проверяем кнопку в тулбаре поля output-text
        StandardField<?> outputText = (StandardField<?>) form.getComponent().getFieldsets().getFirst().getRows()
                .getFirst().getCols().getFirst().getFields().getFirst();
        ClipboardButton button = (ClipboardButton) outputText.getToolbar()[0].getButtons().getFirst();
        assertThat(button, allOf(
                hasProperty("id", is("testClipboardButton_mi0")),
                hasProperty("data", is("`snils`")),
                hasProperty("type", is(ClipboardButtonDataEnum.HTML)),
                hasProperty("message", is("Скопировано в буфер обмена")),
                hasProperty("hint", is("Копировать СНИЛС")),
                hasProperty("icon", is("fa fa-copy")),
                hasProperty("iconPosition", is(PositionEnum.LEFT)),
                hasProperty("color", is("link")),
                hasProperty("datasource", is("testClipboardButton_ds1")),
                hasProperty("src", is("Buttons/Clipboard"))
        ));
        assertThat(button.getConditions().get(ValidationTypeEnum.ENABLED).size(), is(2));
        assertThat(button.getConditions().get(ValidationTypeEnum.VISIBLE).size(), is(1));

        // Проверяем кнопку в основном тулбаре формы
        button = (ClipboardButton) form.getToolbar().getButton("testClipboardButton_mi1");
        assertThat(button, allOf(
                hasProperty("id", is("testClipboardButton_mi1")),
                hasProperty("data", is("Текст")),
                hasProperty("type", is(ClipboardButtonDataEnum.TEXT)),
                hasProperty("message", nullValue()),
                hasProperty("icon", is("fa fa-check")),
                hasProperty("iconPosition", is(PositionEnum.RIGHT)),
                hasProperty("color", is("primary")),
                hasProperty("src", is("Buttons/Clipboard"))

        ));
        assertThat(button.getConditions().get(ValidationTypeEnum.ENABLED).size(), is(1));
        assertThat(button.getConditions().get(ValidationTypeEnum.VISIBLE), is(nullValue()));
    }
}

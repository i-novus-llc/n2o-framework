package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.control.N2oMarkdown;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидатора {@link N2oMarkdown} компонентов
 */
public class MarkdownValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack(), new N2oAllValidatorsPack());
    }

    /**
     * Проверка наличия тега <actions> на странице при указанном actions
     */
    @Test
    void testMissedMarkdownActions() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/markdown/testMissedMarkdownActions.page.xml"),
                "Для компонента с actions=\"action1,action2\" не найдены действия <actions>"
        );
    }

    /**
     * Проверка существования действия с идентификатором, указанным в actions
     */
    @Test
    void testActionExistenceByActionId() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/markdown/testExistenceMarkdownActions.page.xml"),
                "Компонент с actions \"action2\" ссылается на несуществующее действие"
        );
    }

    /**
     * Проверка существования действия с идентификатором, указанным в actions
     * когда сам action на странице или в виджете
     */
    @Test
    void testActionExist() {
        validate("net/n2oapp/framework/config/metadata/validation/markdown/testActionInPageMarkdownActions.page.xml");
        validate("net/n2oapp/framework/config/metadata/validation/markdown/testActionInWidgetMarkdownActions.page.xml");
    }
}

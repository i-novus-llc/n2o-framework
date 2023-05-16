package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валдитора {@link ActionsAware} компонентов
 */
public class ActionsAwareValidatorTest extends SourceValidationTestBase {

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
     * Проверка наличия тега <actions> на странице при указанном action-id
     */
    @Test
    void testMissedPageActions() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/aware/testMissedPageActions.page.xml"),
                "Для компонента с action-id=\"test\" не найдены действия <actions>"
        );
    }

    /**
     * Проверка наличия тега <actions> на в виджете при указанном action-id
     */
    @Test
    void testMissedWidgetActions() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/aware/testMissedPageActions.page.xml"),
                "Для компонента с action-id=\"test\" не найдены действия <actions>"
        );
    }

    /**
     * Проверка существования действия с идентификатором, указанным в action-id
     */
    @Test
    void testActionExistenceByActionId() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/aware/testActionExistenceByActionId.page.xml"),
                "Компонент с action-id=\"test\" ссылается на несуществующее действие test"
        );
    }

    /**
     * Проверка использования действия внутри компонента и action-id одновременно
     */
    @Test
    void testUsingActionIdAndActionAtTheSameTime() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/aware/testUsingActionIdAndActionAtTheSameTime.page.xml"),
                "Компонент с action-id=\"test\" содержит действия и использует ссылку action-id одновременно"
        );
    }
}

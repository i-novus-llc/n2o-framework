package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валдитора {@link ActionsAware} компонентов
 */
public class ActionsAwareValidatorTest extends SourceValidationTestBase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    @Before
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
    public void testMissedPageActions() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компонента с action-id=\"test\" не найдены действия <actions>");
        validate("net/n2oapp/framework/config/metadata/validation/action/aware/testMissedPageActions.page.xml");
    }

    /**
     * Проверка наличия тега <actions> на в виджете при указанном action-id
     */
    @Test
    public void testMissedWidgetActions() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компонента с action-id=\"test\" не найдены действия <actions>");
        validate("net/n2oapp/framework/config/metadata/validation/action/aware/testMissedPageActions.page.xml");
    }

    /**
     * Проверка существования действия с идентификатором, указанным в action-id
     */
    @Test
    public void testActionExistenceByActionId() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Компонент с action-id=\"test\" ссылается на несуществующее действие test");
        validate("net/n2oapp/framework/config/metadata/validation/action/aware/testActionExistenceByActionId.page.xml");
    }

    /**
     * Проверка использования действия внутри компонента и action-id одновременно
     */
    @Test
    public void testUsingActionIdAndActionAtTheSameTime() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Компонент с action-id=\"test\" содержит действия и использует ссылку action-id одновременно");
        validate("net/n2oapp/framework/config/metadata/validation/action/aware/testUsingActionIdAndActionAtTheSameTime.page.xml");
    }
}

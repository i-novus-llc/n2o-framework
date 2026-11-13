package net.n2oapp.framework.config.metadata.validation.event;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.datasource.StompDatasourceIO;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllEventsPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.validation.standard.action.AlertActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.application.ApplicationValidator;
import net.n2oapp.framework.config.metadata.validation.standard.event.StompEventValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации события, приходящего через STOMP протокол
 */
class StompEventValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oActionsPack(), new N2oAllEventsPack());
        builder.ios(new StompDatasourceIO());
        builder.validators(new ApplicationValidator(), new StompEventValidator(), new AlertActionValidator());
    }

    /**
     * Проверка валидации действия внутри stomp-event
     */
    @Test
    void testActionValidation() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/event/stomp/testStompEventActionValidation.application.xml"));
        assertEquals("Действие \"<alert>\" использует недопустимое значение атрибута 'color=\"red\"'", exception.getMessage());
    }

    @Test
    void testEmptyDestination() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/event/stomp/testStompEventEmptyDestination.application.xml"));
        assertEquals("В событии \"<stomp-event>\" не задан обязательный атрибут 'destination'", exception.getMessage());
    }
}

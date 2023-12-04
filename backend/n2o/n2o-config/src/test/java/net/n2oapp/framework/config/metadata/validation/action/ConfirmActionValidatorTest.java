package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfirmActionValidatorTest extends SourceValidationTestBase {

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

    @Test
    void testConfirmAction() {
        validate("net/n2oapp/framework/config/metadata/validation/action/confirm/testConfirmAction.page.xml");
    }

    @Test
    void testOneButtonOk() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/confirm/testOneButtonOk.page.xml"));
        assertEquals("В действии <confirm> указана кнопка <ok>, но не указана кнопка <cancel>", exception.getMessage());
    }

    @Test
    void testOneButtonCancel() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/confirm/testOneButtonCancel.page.xml"));
        assertEquals("В действии <confirm> указана кнопка <cancel>, но не указана кнопка <ok>", exception.getMessage());
    }

    @Test
    void testTwoOkButtons() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/confirm/testTwoOkButtons.page.xml"));
        assertEquals("В действии <confirm> указаны две кнопки <ok>", exception.getMessage());
    }

    @Test
    void testTwoCancelButtons() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/confirm/testTwoCancelButtons.page.xml"));
        assertEquals("В действии <confirm> указаны две кнопки <cancel>", exception.getMessage());
    }
    @Test
    void testColorButtons() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/confirm/testColorButton.page.xml"));
        assertEquals("Одна из кнопок действия <confirm> использует недопустимое значение атрибута color='test'", exception.getMessage());
    }

}

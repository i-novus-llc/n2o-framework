package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateActionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack(), new N2oAllValidatorsPack());
    }

    @Test
    void testEmptyFieldId() {
        var exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/validate/testEmptyFieldId.page.xml"));
        assertEquals("В действии \"<validate>\" в элементе \"<field>\" не задан обязательный атрибут 'id'",
                exception.getMessage());
    }
}
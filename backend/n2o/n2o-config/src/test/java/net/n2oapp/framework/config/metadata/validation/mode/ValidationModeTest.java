package net.n2oapp.framework.config.metadata.validation.mode;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationModeTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oActionsPack(), new N2oAllValidatorsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/action/page/formRef.widget.xml"));
    }

    @Test
    void testModeOn() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.validation.mode", "on");
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/mode/testValidationMode.page.xml")
        );
        assertEquals("Атрибут \"refresh-datasources\" ссылается на несуществующий источник данных 'ds3'", exception.getMessage());
    }

    @Test
    void testModeIgnoreRefs() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.validation.mode", "ignore-refs");
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/mode/testValidationMode.page.xml")
        );
        assertEquals("Атрибут \"refresh-datasources\" ссылается на несуществующий источник данных 'ds4'", exception.getMessage());
    }

    @Test
    void testModeOff() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.validation.mode", "off");
        validate("net/n2oapp/framework/config/metadata/validation/mode/testValidationMode.page.xml");
    }

    @Test
    void testWrongValueMode() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.validation.mode", "wrong");
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/mode/testValidationMode.page.xml")
        );
        assertEquals("Недопустимое значение настройки 'n2o.validation.mode'='wrong'. Допустимые значения: [on | ignore-refs | off]",
                exception.getMessage());
    }
}

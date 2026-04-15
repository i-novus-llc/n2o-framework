package net.n2oapp.framework.access.metadata.validation;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllValidatorsPack;
import net.n2oapp.framework.config.metadata.pack.N2oCellsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тест валидации security атрибутов для ячеек
 */
class CellSecurityValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oCellsPack());
        builder.packs(new N2oAllValidatorsPack());
        builder.packs(new AccessSchemaPack());
    }

    @Test
    void testCellSecurityWithBehaviorDisable() {
        assertDoesNotThrow(() -> validate(
                "net/n2oapp/framework/access/metadata/validation/cellSecurityValidBehaviorDisable.page.xml"));
    }

    @Test
    void testCellSecurityWithoutBehavior() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/cellSecurityInvalidNoBehavior.page.xml"));
        assertEquals("Для ячейки с security атрибутами необходимо указать sec:behavior=\"disable\" " +
                "или поведение disable должно быть задано по умолчанию настройкой n2o.access.behavior", exception.getMessage());
    }

    @Test
    void testDndColumnCellSecurityWithoutBehavior() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/dndColumnCellSecurityInvalidNoBehavior.page.xml"));
        assertEquals("Для ячейки с security атрибутами необходимо указать sec:behavior=\"disable\" " +
                "или поведение disable должно быть задано по умолчанию настройкой n2o.access.behavior", exception.getMessage());
    }

    @Test
    void testCellSecurityWithBehaviorHide() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/cellSecurityInvalidBehaviorHide.page.xml"));
        assertEquals("Для ячейки с security атрибутами необходимо указать sec:behavior=\"disable\" " +
                "или поведение disable должно быть задано по умолчанию настройкой n2o.access.behavior", exception.getMessage());
    }

    @Test
    void testCellWithoutSecurity() {
        assertDoesNotThrow(() -> validate(
                "net/n2oapp/framework/access/metadata/validation/cellNoSecurity.page.xml"));
    }
}

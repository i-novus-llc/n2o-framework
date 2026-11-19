package net.n2oapp.framework.access.metadata;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.access.metadata.schema.N2oAccessSchema;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тест валидации схемы доступа
 */
class SimpleAccessSchemaValidationTest extends SourceValidationTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder b) {
        super.configure(b);
        b.types(new MetaType("access", N2oAccessSchema.class));
        b.validators(new SimpleAccessSchemaValidator());
        b.packs(new AccessSchemaPack(), new N2oObjectsPack());
        b.sources(new CompileInfo("net/n2oapp/framework/access/metadata/atBlank.object.xml"));
        b.sources(new CompileInfo("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml"));
    }

    @Test
    void testValid() {
        validate("net/n2oapp/framework/access/metadata/validation.access.xml");
    }

    @Test
    void testInvalid2() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation2.access.xml")
        );
        assertEquals("Объект 'atWrong', заданный в object-access не существует", exception.getMessage());
    }

    @Test
    void testInvalid5() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation5.access.xml")
        );
        assertEquals("Страница 'notExistingPage', заданная в page-access не существует", exception.getMessage());
    }

    @Test
    void testPermissionWithoutId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/required/permissionWithoutId.access.xml")
        );
        assertEquals("В элементе \"<permission>\" схемы доступа не задан обязательный атрибут 'id'", exception.getMessage());
    }

    @Test
    void testRoleWithoutId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/required/roleWithoutId.access.xml")
        );
        assertEquals("В элементе \"<role>\" схемы доступа не задан обязательный атрибут 'id'", exception.getMessage());
    }

    @Test
    void testUserWithoutUsername() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/required/userWithoutUsername.access.xml")
        );
        assertEquals("В элементе \"<user>\" схемы доступа не задан обязательный атрибут 'username'", exception.getMessage());
    }

    @Test
    void testObjectFiltersWithoutObjectId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/required/objectFiltersWithoutObjectId.access.xml")
        );
        assertEquals("В элементе \"<object-filters>\" схемы доступа не задан обязательный атрибут 'object-id'", exception.getMessage());
    }

    @Test
    void testFilterWithoutId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/required/filterWithoutId.access.xml")
        );
        assertEquals("В фильтре элемента \"<object-filters>\" объекта 'atBlank' не задан обязательный атрибут 'id'", exception.getMessage());
    }

    @Test
    void testFilterWithoutFieldId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/required/filterWithoutFieldId.access.xml")
        );
        assertEquals("В фильтре 'f1' элемента \"<object-filters>\" объекта 'atBlank' не задан обязательный атрибут 'field-id'", exception.getMessage());
    }

    @Test
    void testUrlAccessWithoutPattern() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation/required/urlAccessWithoutPattern.access.xml")
        );
        assertEquals("В элементе \"<url-access>\" схемы доступа не задан обязательный атрибут 'pattern'", exception.getMessage());
    }
}

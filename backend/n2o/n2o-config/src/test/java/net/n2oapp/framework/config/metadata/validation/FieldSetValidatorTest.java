package net.n2oapp.framework.config.metadata.validation;


import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.*;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации филдсетов
 */
public class FieldSetValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.validators(new FormValidator(), new FieldSetRowValidator(), new FieldSetColumnValidator(),
                new SetFieldSetValidator(), new LineFieldSetValidator(),
                new MultiFieldSetValidator(), new FieldValidator());
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    void testUniqueFieldId() {
        validate("net/n2oapp/framework/config/metadata/validation/fieldset/testUniqueFieldId.widget.xml");
    }

    @Test
    void testNonUniqueFieldId() {
        validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldId.widget.xml");
    }

    @Test
    void testNonUniqueFieldIdWithDependencies() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldIdWithDependencies.widget.xml"));
        assertEquals("Поле 'test1' встречается более одного раза", exception.getMessage());
    }

    @Test
    void testNonUniqueFieldIdWithDependenciesWithMultiSet() {
        validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldIdWithDependencies2.widget.xml");
    }

    @Test
    void testNonUniqueFieldIdWithDependenciesInMultiSet() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldIdInMultiSet.widget.xml"));
        assertEquals("Поле 'test1' встречается более одного раза", exception.getMessage());
    }

    @Test
    void testInvalidColumnSize() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testInvalidColumnSize.widget.xml"));
        assertEquals("Размер колонки филдсета виджета 'testInvalidColumnSize' должен иметь значение от 1 до 12", exception.getMessage());
    }

    @Test
    void testSetFieldSetBadgeColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testSetFieldSetBadgeColor.widget.xml"));
        assertEquals("Филдсет <set> использует недопустимое значение атрибута badge-color=\"red\"", exception.getMessage());
    }

    @Test
    void testLineFieldSetBadgeColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testLineFieldSetBadgeColor.widget.xml"));
        assertEquals("Филдсет <line> использует недопустимое значение атрибута badge-color=\"red\"", exception.getMessage());
    }

    @Test
    void testMultiFieldSetBadgeColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testMultiFieldSetBadgeColor.widget.xml"));
        assertEquals("Филдсет <multi-set> использует недопустимое значение атрибута badge-color=\"red\"", exception.getMessage());
    }

    @Test
    void testEmptyMultiSet() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testEmptyMultiSet.widget.xml"));
        assertEquals("Мультифилдсет 'empty' виджета 'testEmptyMultiSet' имеет пустое тело", exception.getMessage());
    }

    @Test
    void testMultisetWithoutId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testMultisetWithoutId.widget.xml"));
        assertEquals("Мультифилдсет виджета 'testMultisetWithoutId' не имеет идентификатора", exception.getMessage());
    }

    @Test
    void testEmptyCol() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testEmptyCol.widget.xml"));
        assertEquals("Для <сol> виджета 'testEmptyCol' необходимо задать поля, либо же атрибут 'size'", exception.getMessage());
    }

    @Test
    void testEmptyColValid() {
        validate("net/n2oapp/framework/config/metadata/validation/fieldset/testEmptyColValid.widget.xml");
    }

    @Test
    void testEmptyRow() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testEmptyRow.widget.xml"));
        assertEquals("Для <row> виджета 'testEmptyRow' необходимо задать поля", exception.getMessage());
    }
}

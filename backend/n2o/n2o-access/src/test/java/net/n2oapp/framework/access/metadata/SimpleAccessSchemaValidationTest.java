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

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тест валидации схемы доступа
 */
public class SimpleAccessSchemaValidationTest extends SourceValidationTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
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
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation2.access.xml")       
        );
    }

    @Test
    void testInvalid5() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/access/metadata/validation5.access.xml")
        );
    }
}

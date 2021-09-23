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
import org.junit.Before;
import org.junit.Test;

/**
 * Тест валидации схемы доступа
 */
public class SimpleAccessSchemaValidationTest extends SourceValidationTestBase {
    @Override
    @Before
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
    public void testValid() throws Exception {
        validate("net/n2oapp/framework/access/metadata/validation.access.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testInvalid2() throws Exception {
        validate("net/n2oapp/framework/access/metadata/validation2.access.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testInvalid5() throws Exception {
        validate("net/n2oapp/framework/access/metadata/validation5.access.xml");
    }
}

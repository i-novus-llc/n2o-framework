package net.n2oapp.framework.config.metadata.compile.object.validation;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции валидации обязательности заполнения поля
 */
class MandatoryValidationCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack());
    }

    @Test
    void testMandatoryValidation() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/validation/testMandatoryValidation.object.xml")
                .get(new ObjectContext("testMandatoryValidation"));

        List<Validation> validations = object.getValidations();
        assertThat(validations.size(), is(2));

        assertThat(validations.get(0).getId(), is("man1"));
        assertThat(validations.get(0).getSeverity(), is(SeverityTypeEnum.danger));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMomentEnum.afterFailOperation));
        assertThat(validations.get(0).getMessage(), is("message"));
        assertThat(validations.get(0).getSide(), is("client,server"));
        assertThat(validations.get(0).getFieldId(), is("field1"));
        assertThat(validations.get(0).getEnablingConditions(), hasItem("field2 != null"));

        assertThat(validations.get(1).getId(), is("man2"));
        assertThat(validations.get(1).getSeverity(), is(SeverityTypeEnum.info));
        assertThat(validations.get(1).getMoment(), is(N2oValidation.ServerMomentEnum.afterSuccessOperation));
    }
}

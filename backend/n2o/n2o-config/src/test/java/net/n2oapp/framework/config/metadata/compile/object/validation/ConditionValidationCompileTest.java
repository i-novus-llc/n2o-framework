package net.n2oapp.framework.config.metadata.compile.object.validation;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
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
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции валидации условия значений полей
 */
class ConditionValidationCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack());
    }

    @Test
    void testConditionValidation() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/validation/testConditionValidation.object.xml")
                .get(new ObjectContext("testConditionValidation"));

        List<Validation> validations = object.getValidations();
        assertThat(validations.size(), is(4));

        assertThat(validations.get(0).getId(), is("con1"));
        assertThat(validations.get(0).getSeverity(), is(SeverityTypeEnum.danger));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMomentEnum.afterFailOperation));
        assertThat(validations.get(0).getMessage(), is("message"));
        assertThat(validations.get(0).getEnabled(), is(false));
        assertThat(validations.get(0).getSide(), is("client,server"));
        assertThat(validations.get(0).getFieldId(), is("field1"));
        assertThat(((ConditionValidation) validations.get(0)).getExpressionOn()[0], is("field1"));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("field1 == 'test'"));

        assertThat(validations.get(1).getId(), is("con2"));
        assertThat(validations.get(1).getSeverity(), is(SeverityTypeEnum.info));
        assertThat(validations.get(1).getMoment(), is(N2oValidation.ServerMomentEnum.afterSuccessOperation));
        assertThat(((ConditionValidation) validations.get(1)).getExpression(), is("field1 == 'test'"));

        assertThat(validations.get(2).getId(), is("con3"));
        assertThat(validations.get(2).getSeverity(), is(SeverityTypeEnum.warning));
        assertThat(validations.get(2).getMoment(), is(N2oValidation.ServerMomentEnum.beforeOperation));

        assertThat(validations.get(3).getSeverity(), is(SeverityTypeEnum.danger));
    }
}

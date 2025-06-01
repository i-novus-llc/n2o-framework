package net.n2oapp.framework.config.metadata.compile.object.validation;

import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование компиляции валидации ограничений полей
 */
class ConstraintValidationCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack());
        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    void testConstraintValidation() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/validation/testConstraintValidation.object.xml")
                .get(new ObjectContext("testConstraintValidation"));

        List<Validation> validations = object.getValidations();
        assertThat(validations.size(), is(2));

        assertThat(validations.get(0).getId(), is("con1"));
        assertThat(validations.get(0).getSeverity(), is(SeverityTypeEnum.DANGER));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMomentEnum.AFTER_FAIL_OPERATION));
        assertThat(validations.get(0).getMessage(), is("message"));
        assertThat(validations.get(0).getEnabled(), is(false));
        assertThat(validations.get(0).getSide(), is("client,server"));
        assertThat(validations.get(0).getFieldId(), is("field1"));

        N2oTestDataProvider invocation = (N2oTestDataProvider) ((ConstraintValidation) validations.get(0)).getInvocation();
        assertThat(invocation.getOperation(), is(N2oTestDataProvider.OperationEnum.CREATE));
        assertThat(invocation.getFile(), is("test.json"));

        List<AbstractParameter> inParametersList = ((ConstraintValidation) validations.get(0)).getInParametersList();
        assertThat(inParametersList.size(), is(3));
        Set<String> requiredFields = validations.get(0).getRequiredFields();
        assertThat(requiredFields.size(), is(1));
        assertThat(requiredFields.contains("name"), is(true));
        List<AbstractParameter> outParametersList = ((ConstraintValidation) validations.get(0)).getOutParametersList();
        assertThat(outParametersList.size(), is(2));
        assertThat(outParametersList.get(0).getId(), is("id"));
        assertThat(outParametersList.get(1).getId(), is("validation"));
        assertThat(outParametersList.get(1).getMapping(), is("name=='test'"));


        assertThat(validations.get(1).getId(), is("con2"));
        assertThat(validations.get(1).getSeverity(), is(SeverityTypeEnum.INFO));
        assertThat(validations.get(1).getMoment(), is(N2oValidation.ServerMomentEnum.AFTER_SUCCESS_OPERATION));
        outParametersList = ((ConstraintValidation) validations.get(1)).getOutParametersList();
        assertThat(outParametersList.size(), is(1));
        assertThat(outParametersList.get(0).getId(), is("validation"));
        assertThat(outParametersList.get(0).getMapping(), nullValue());
    }
}

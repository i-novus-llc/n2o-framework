package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.ValidationDialog;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.JavaDataProviderIOv1;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Тестирование компиляции объекта
 */
public class ObjectCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack())
                .ios(new JavaDataProviderIOv1())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"));
    }

    @Test
    public void testCompileOperations() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml")
                .get(new ObjectContext("utAction"));
        assertThat(object.getOperations().size(), is(2));
        assertThat(object.getOperations().containsKey("create"), is(true));
        assertThat(object.getOperations().containsKey("delete"), is(true));

        CompiledObject.Operation createOperation = object.getOperations().get("create");
        N2oJavaDataProvider provider = (N2oJavaDataProvider) createOperation.getInvocation();
        assertThat(provider.getClassName(), is("TestService"));
        assertThat(provider.getArguments()[0].getName(), is("arg1"));
        assertThat(provider.getArguments()[0].getClassName(), is("TestEntity"));
        assertThat(provider.getArguments()[0].getType(), is(Argument.Type.ENTITY));
        assertThat(provider.getArguments()[1].getName(), is("arg2"));
        assertThat(provider.getArguments()[1].getClassName(), is("EntityClass"));
        assertThat(provider.getArguments()[1].getType(), is(Argument.Type.ENTITY));
        assertThat(provider.getArguments()[2].getName(), is("arg3"));
        assertThat(provider.getArguments()[2].getClassName(), nullValue());
        assertThat(provider.getArguments()[2].getType(), is(Argument.Type.PRIMITIVE));

        assertThat(createOperation.getInParametersMap().size(), is(2));
        assertThat(createOperation.getInParametersMap().containsKey("in1"), is(true));
        assertThat(createOperation.getInParametersMap().containsKey("in2"), is(true));
        assertThat(createOperation.getOutParametersMap().size(), is(1));
        assertThat(createOperation.getOutParametersMap().containsKey("out"), is(true));
        assertThat(createOperation.getFailOutParametersMap().size(), is(1));
        assertThat(createOperation.getFailOutParametersMap().containsKey("code"), is(true));
        assertThat(createOperation.getFailOutParametersMap().get("code").getMapping(), is("#this.getCode()"));
    }

    @Test
    public void testCompileValidations() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/utValidation.object.xml")
                .get(new ObjectContext("utValidation"));
        assertThat(object.getValidations().size(), is(3));
        assertThat(object.getValidationsMap().get("v1").getId(), is("v1"));
        assertThat(object.getValidationsMap().get("v2").getId(), is("v2"));
        assertThat(object.getValidationsMap().get("v3").getId(), is("v3"));

        assertThat(object.getOperations().size(), is(2));
        CompiledObject.Operation all = object.getOperations().get("all");
        assertThat(all.getValidationList().size(), is(4));
        assertThat(all.getValidationsMap().get("val1"), instanceOf(ConstraintValidation.class));
        assertThat(all.getValidationsMap().get("v1"), instanceOf(ConditionValidation.class));
        assertThat(all.getValidationsMap().get("v2"), instanceOf(ConstraintValidation.class));
        assertThat(all.getValidationsMap().get("v3"), instanceOf(ValidationDialog.class));

        CompiledObject.Operation black = object.getOperations().get("black");
        assertThat(black.getValidationList().size(), is(2));
        assertThat(black.getValidationsMap().containsKey("v2"), is(true));
        assertThat(black.getValidationsMap().containsKey("v3"), is(true));

        N2oJavaDataProvider val1Provider =
                (N2oJavaDataProvider) ((ConstraintValidation) all.getValidationsMap().get("val1")).getInvocation();
        assertThat(val1Provider.getClassName(), is("TestService"));
        assertThat(val1Provider.getArguments()[0].getName(), is("arg1"));
        assertThat(val1Provider.getArguments()[0].getClassName(), is("TestEntity"));
        assertThat(val1Provider.getArguments()[0].getType(), is(Argument.Type.ENTITY));
        assertThat(val1Provider.getArguments()[1].getName(), is("arg2"));
        assertThat(val1Provider.getArguments()[1].getClassName(), is("EntityClass"));
        assertThat(val1Provider.getArguments()[1].getType(), is(Argument.Type.ENTITY));
        assertThat(val1Provider.getArguments()[2].getName(), is("arg3"));
        assertThat(val1Provider.getArguments()[2].getClassName(), nullValue());
        assertThat(val1Provider.getArguments()[2].getType(), is(Argument.Type.PRIMITIVE));

        N2oJavaDataProvider v2Provider =
                (N2oJavaDataProvider) ((ConstraintValidation) all.getValidationsMap().get("v2")).getInvocation();
        assertThat(v2Provider.getClassName(), is("TestService"));
        assertThat(v2Provider.getArguments()[0].getName(), is("arg1"));
        assertThat(v2Provider.getArguments()[0].getClassName(), is("TestEntity"));
        assertThat(v2Provider.getArguments()[0].getType(), is(Argument.Type.ENTITY));
        assertThat(v2Provider.getArguments()[1].getName(), is("arg2"));
        assertThat(v2Provider.getArguments()[1].getClassName(), is("EntityClass"));
        assertThat(v2Provider.getArguments()[1].getType(), is(Argument.Type.ENTITY));
        assertThat(v2Provider.getArguments()[2].getName(), is("arg3"));
        assertThat(v2Provider.getArguments()[2].getClassName(), nullValue());
        assertThat(v2Provider.getArguments()[2].getType(), is(Argument.Type.PRIMITIVE));

        N2oJavaDataProvider v3Provider =
                (N2oJavaDataProvider) ((ValidationDialog) all.getValidationsMap().get("v3")).getInvocation();
        assertThat(v3Provider.getClassName(), is("TestService"));
        assertThat(v3Provider.getArguments()[0].getName(), is("arg1"));
        assertThat(v3Provider.getArguments()[0].getClassName(), is("TestEntity"));
        assertThat(v3Provider.getArguments()[0].getType(), is(Argument.Type.ENTITY));
        assertThat(v3Provider.getArguments()[1].getName(), is("arg2"));
        assertThat(v3Provider.getArguments()[1].getClassName(), is("EntityClass"));
        assertThat(v3Provider.getArguments()[1].getType(), is(Argument.Type.ENTITY));
        assertThat(v3Provider.getArguments()[2].getName(), is("arg3"));
        assertThat(v3Provider.getArguments()[2].getClassName(), nullValue());
        assertThat(v3Provider.getArguments()[2].getType(), is(Argument.Type.PRIMITIVE));
    }

    @Test
    public void testCompileFields() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/utObjectField.object.xml")
                .get(new ObjectContext("utObjectField"));
        assertThat(object.getObjectFields().size(), is(2));

        AbstractParameter field = object.getObjectFieldsMap().get("f1");
        assertThat(field.getId(), is("f1"));
        assertThat(field.getRequired(), is(true));
        assertThat(field.getMapping(), is("['test']"));
        assertThat(((ObjectSimpleField) field).getDomain(), is("integer"));

        field = object.getObjectFieldsMap().get("f2");
        assertThat(field.getId(), is("f2"));
        assertThat(((ObjectReferenceField) field).getReferenceObjectId(), is("utAction"));

        field = object.getOperations().get("create").getInParametersMap().get("f1");
        assertThat(field.getMapping(), is("['test']"));
        assertThat(((ObjectSimpleField) field).getDomain(), is("integer"));
    }
}

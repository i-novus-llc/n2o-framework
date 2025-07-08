package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSetField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.JavaDataProviderIOv1;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

/**
 * Тестирование компиляции объекта
 */
class ObjectCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
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
    void testCompileOperations() {
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
        assertThat(provider.getArguments()[0].getType(), is(Argument.TypeEnum.ENTITY));
        assertThat(provider.getArguments()[1].getName(), is("arg2"));
        assertThat(provider.getArguments()[1].getClassName(), is("EntityClass"));
        assertThat(provider.getArguments()[1].getType(), is(Argument.TypeEnum.ENTITY));
        assertThat(provider.getArguments()[2].getName(), is("arg3"));
        assertThat(provider.getArguments()[2].getClassName(), nullValue());
        assertThat(provider.getArguments()[2].getType(), is(Argument.TypeEnum.PRIMITIVE));

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
    void testCompileValidations() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/utValidation.object.xml")
                .get(new ObjectContext("utValidation"));
        assertThat(object.getValidations().size(), is(2));
        assertThat(object.getValidationsMap().get("v1").getId(), is("v1"));
        assertThat(object.getValidationsMap().get("v2").getId(), is("v2"));

        assertThat(object.getOperations().size(), is(2));
        CompiledObject.Operation all = object.getOperations().get("all");
        assertThat(all.getValidationList().size(), is(3));
        assertThat(all.getValidationsMap().get("val1"), instanceOf(ConstraintValidation.class));
        assertThat(all.getValidationsMap().get("v1"), instanceOf(ConditionValidation.class));
        assertThat(all.getValidationsMap().get("v2"), instanceOf(ConstraintValidation.class));

        CompiledObject.Operation black = object.getOperations().get("black");
        assertThat(black.getValidationList().size(), is(1));
        assertThat(black.getValidationsMap().containsKey("v2"), is(true));

        N2oJavaDataProvider val1Provider =
                (N2oJavaDataProvider) ((ConstraintValidation) all.getValidationsMap().get("val1")).getInvocation();
        assertThat(val1Provider.getClassName(), is("TestService"));
        assertThat(val1Provider.getArguments()[0].getName(), is("arg1"));
        assertThat(val1Provider.getArguments()[0].getClassName(), is("TestEntity"));
        assertThat(val1Provider.getArguments()[0].getType(), is(Argument.TypeEnum.ENTITY));
        assertThat(val1Provider.getArguments()[1].getName(), is("arg2"));
        assertThat(val1Provider.getArguments()[1].getClassName(), is("EntityClass"));
        assertThat(val1Provider.getArguments()[1].getType(), is(Argument.TypeEnum.ENTITY));
        assertThat(val1Provider.getArguments()[2].getName(), is("arg3"));
        assertThat(val1Provider.getArguments()[2].getClassName(), nullValue());
        assertThat(val1Provider.getArguments()[2].getType(), is(Argument.TypeEnum.PRIMITIVE));
    }

    @Test
    void testCompileFields() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/testObjectField.object.xml",
                "net/n2oapp/framework/config/metadata/compile/object/entity.object.xml",
                "net/n2oapp/framework/config/metadata/compile/object/entity2.object.xml",
                "net/n2oapp/framework/config/metadata/compile/object/rating.object.xml",
                "net/n2oapp/framework/config/metadata/compile/object/set.object.xml")
                .get(new ObjectContext("testObjectField"));
        assertThat(object.getObjectFields().size(), is(4));

        // 1. object fields
        checkObjectFields(object);

        // 2. operation1 fields (defined in operation and use object fields attribute definition by default)
        checkOperation1Fields(object);

        // 3. operation2 fields (without attributes and body use object fields definition)
        checkOperation2Fields(object);
    }

    private static void checkOperation2Fields(CompiledObject object) {
        Map<String, AbstractParameter> op2InFields = object.getOperations().get("op2").getInParametersMap();
        assertThat(op2InFields.size(), is(3));

        ObjectReferenceField refField = (ObjectReferenceField) op2InFields.get("entity");
        assertThat(refField, allOf(
                hasProperty("id", is("entity")),
                hasProperty("mapping", is("test2")),
                hasProperty("required", is(false)),
                hasProperty("referenceObjectId", nullValue()),
                hasProperty("entityClass", is("com.example.Object")),
                hasProperty("fields", arrayWithSize(2))
        ));

        ObjectSimpleField child = (ObjectSimpleField) refField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("id")),
                hasProperty("mapping", is("code")),
                hasProperty("domain", is("integer")),
                hasProperty("required", is(true)),
                hasProperty("defaultValue", is("1")),
                hasProperty("normalize", is("#this > 0 ? #this : -1"))
        ));

        ObjectReferenceField refChild = (ObjectReferenceField) refField.getFields()[1];
        assertThat(refChild, allOf(
                hasProperty("id", is("rating")),
                hasProperty("mapping", is("userRating")),
                hasProperty("fields", arrayWithSize(2))
        ));
        // fields defined in reference object for reference object
        child = (ObjectSimpleField) refChild.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("code")),
                hasProperty("domain", is("string"))
        ));
        child = (ObjectSimpleField) refChild.getFields()[1];
        assertThat(child, allOf(
                hasProperty("id", is("value")),
                hasProperty("domain", is("numeric"))
        ));


        ObjectListField listField = (ObjectListField) op2InFields.get("list");
        assertThat(listField, allOf(
                hasProperty("id", is("list")),
                hasProperty("mapping", is("test3")),
                hasProperty("required", is(true)),
                hasProperty("fields", arrayWithSize(1))
        ));
        child = (ObjectSimpleField) listField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("id")),
                hasProperty("mapping", is("pk")),
                hasProperty("required", is(true))
        ));

        ObjectSetField setField = (ObjectSetField) op2InFields.get("set");
        assertThat(setField, allOf(
                hasProperty("id", is("set")),
                hasProperty("mapping", is("test4")),
                hasProperty("required", is(false)),
                hasProperty("fields", arrayWithSize(1))
        ));
        child = (ObjectSimpleField) setField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("name")),
                hasProperty("mapping", is("name2")),
                hasProperty("required", is(true)),
                hasProperty("domain", is("string"))
        ));
    }

    private static void checkOperation1Fields(CompiledObject object) {

        Map<String, AbstractParameter> op1InFields = object.getOperations().get("op1").getInParametersMap();
        assertThat(op1InFields.size(), is(5));

        ObjectSimpleField field = (ObjectSimpleField) op1InFields.get("f1");
        assertThat(field, allOf(
                hasProperty("id", is("f1")),
                hasProperty("required", is(false)),
                hasProperty("mapping", is("f1")),
                hasProperty("domain", is("string"))
        ));

        field = (ObjectSimpleField) op1InFields.get("f2");
        assertThat(field, allOf(
                hasProperty("id", is("f2")),
                hasProperty("required", is(false)),
                hasProperty("mapping", is("f2")),
                hasProperty("domain", is("string"))
        ));

        ObjectReferenceField refField = (ObjectReferenceField) op1InFields.get("entity");
        assertThat(refField, allOf(
                hasProperty("id", is("entity")),
                hasProperty("mapping", is("entity2")),
                hasProperty("required", is(false)),
                hasProperty("referenceObjectId", nullValue()),
                hasProperty("entityClass", is("com.example.Object")),
                hasProperty("fields", arrayWithSize(2))
        ));
        ObjectSimpleField child = (ObjectSimpleField) refField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("id")),
                hasProperty("mapping", is("code")),
                // after merged with reference object field
                // attribute priority: in operation field -> in operation object-id field -> object field
                hasProperty("domain", is("short")),
                hasProperty("required", is(false)),
                // after merged with object field
                hasProperty("defaultValue", is("1")),
                hasProperty("normalize", is("#this > 0 ? #this : -1"))
        ));

        child = (ObjectSimpleField) refField.getFields()[1];
        assertThat(child, allOf(
                hasProperty("id", is("name")),
                hasProperty("mapping", is("title")),
                // from reference object field
                hasProperty("domain", is("string")),
                hasProperty("required", nullValue()),
                hasProperty("defaultValue", nullValue()),
                hasProperty("normalize", nullValue())
        ));

        ObjectListField listField = (ObjectListField) op1InFields.get("list");
        assertThat(listField, allOf(
                hasProperty("id", is("list")),
                hasProperty("mapping", is("list")),
                hasProperty("required", is(false)),
                hasProperty("fields", arrayWithSize(1))
        ));
        child = (ObjectSimpleField) listField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("id")),
                hasProperty("mapping", is("id2")),
                hasProperty("required", is(false))
        ));

        ObjectSetField setField = (ObjectSetField) op1InFields.get("set");
        assertThat(setField, allOf(
                hasProperty("id", is("set")),
                hasProperty("mapping", is("set")),
                hasProperty("required", is(true)),
                hasProperty("fields", arrayWithSize(1))
        ));
        child = (ObjectSimpleField) setField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("name")),
                hasProperty("mapping", is("title")),
                hasProperty("domain", is("string"))
        ));
    }

    private static void checkObjectFields(CompiledObject object) {
        // 1. object fields
        ObjectSimpleField field = (ObjectSimpleField) object.getObjectFieldsMap().get("f1");
        assertThat(field, allOf(
                hasProperty("id", is("f1")),
                hasProperty("required", is(true)),
                hasProperty("mapping", is("test")),
                hasProperty("domain", is("integer"))
        ));

        ObjectReferenceField refField = (ObjectReferenceField) object.getObjectFieldsMap().get("entity");
        assertThat(refField, allOf(
                hasProperty("id", is("entity")),
                hasProperty("mapping", is("test2")),
                hasProperty("required", is(false)),
                // define in reference object
                hasProperty("referenceObjectId", nullValue()),
                hasProperty("entityClass", is("com.example.Object")),
                hasProperty("fields", arrayWithSize(2))
        ));
        // field defined in reference object
        ObjectSimpleField child = (ObjectSimpleField) refField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("id")),
                hasProperty("mapping", is("code")),
                hasProperty("domain", is("integer")),
                hasProperty("required", is(true)),
                hasProperty("defaultValue", is("1")),
                hasProperty("normalize", is("#this > 0 ? #this : -1"))
        ));
        ObjectReferenceField refChild = (ObjectReferenceField) refField.getFields()[1];
        assertThat(refChild, allOf(
                hasProperty("id", is("rating")),
                hasProperty("mapping", is("userRating")),
                hasProperty("fields", arrayWithSize(2))
        ));
        // fields defined in reference object for reference object
        child = (ObjectSimpleField) refChild.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("code")),
                hasProperty("domain", is("string"))
        ));
        child = (ObjectSimpleField) refChild.getFields()[1];
        assertThat(child, allOf(
                hasProperty("id", is("value")),
                hasProperty("domain", is("numeric"))
        ));


        ObjectListField listField = (ObjectListField) object.getObjectFieldsMap().get("list");
        assertThat(listField, allOf(
                hasProperty("id", is("list")),
                hasProperty("mapping", is("test3")),
                hasProperty("required", is(true)),
                hasProperty("fields", arrayWithSize(1))
        ));
        child = (ObjectSimpleField) listField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("id")),
                hasProperty("mapping", is("pk")),
                hasProperty("required", is(true))
        ));

        ObjectSetField setField = (ObjectSetField) object.getObjectFieldsMap().get("set");
        assertThat(setField, allOf(
                hasProperty("id", is("set")),
                hasProperty("mapping", is("test4")),
                hasProperty("required", is(false)),
                hasProperty("fields", arrayWithSize(1))
        ));
        child = (ObjectSimpleField) setField.getFields()[0];
        assertThat(child, allOf(
                hasProperty("id", is("name")),
                hasProperty("mapping", is("name2")),  // current object field attribute has higher priority than in reference object
                hasProperty("required", is(true)),    // merged attributes
                hasProperty("domain", is("string"))   // merged attributes
        ));
    }
}

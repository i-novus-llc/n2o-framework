package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/*
 * Тест на компциляцию object-4.0
 */
public class ObjectV4CompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack(), new N2oDataProvidersPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/testObjectRefFields.object.xml"));
    }

    @Test
    public void testCompileActions() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/testObjectOperationsV4.object.xml")
                .get(new ObjectContext("testObjectOperationsV4"));
        CompiledObject.Operation op1 = object.getOperations().get("op1");
        assertThat(object.getOperations().size(), is(2));
        assertThat(op1.getId(), is("op1"));
        assertThat(op1.getName(), is("name"));
        assertThat(op1.getFormSubmitLabel(), is("test"));
        assertThat(op1.getDescription(), is("description"));
        assertThat(op1.getConfirmationText(), is("test"));
        assertThat(op1.getFailText(), is("test"));
        assertThat(op1.getSuccessText(), is("test"));
        assertThat(op1.getConfirm(), is(true));
        assertThat(op1.getInParamsSet().size(), is(1));
        assertThat(op1.getOutParamsSet().size(), is(1));
        assertThat(((N2oSqlDataProvider) op1.getInvocation()).getQuery(), is("select 1"));
        assertThat(object.getOperations().get("op2").getId(), is("op2"));

        N2oObject.Parameter inParam = op1.getInParametersMap().get("id");
        assertThat(inParam.getParam(), is("param"));
        assertThat(inParam.getMapping(), is("mapping"));
        assertThat(inParam.getDefaultValue(), is("val1"));
        assertThat(inParam.getNormalize(), is("norm"));
        assertThat(inParam.getDomain(), is("string"));

        N2oObject.Parameter outParam = op1.getOutParametersMap().get("id");
        assertThat(outParam.getParam(), is(nullValue()));
        assertThat(outParam.getMapping(), is("mapping"));
        assertThat(outParam.getDefaultValue(), is("val2"));
        assertThat(outParam.getNormalize(), is("norm"));
        assertThat(outParam.getDomain(), is("boolean[]"));
    }

}
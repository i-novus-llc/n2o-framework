package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.control.CustomField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StandardFieldAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testQuery.query.xml"),
                new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .packs(new AccessSchemaPack(), new N2oAllDataPack(), new N2oAllPagesPack());
    }

    @Test
    public void standardFieldExtraPropertiesTest() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testRegion");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testRegion.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testStandardFieldAccessTransformer.page.xml");


        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testStandardFieldAccessTransformer"));

        List<FieldSet.Row> rows = ((Form) page.getRegions().get("single").get(0).getContent().get(0)).getComponent().getFieldsets().get(0).getRows();

        StandardField field = (StandardField) rows.get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("field1"));
        assertThat(field.getProperties().size(), is(1));
        assertThat(field.getProperties().get(SECURITY_PROP_NAME), notNullValue());
        assertThat(field.getId(), is("field1"));
        assertThat(field.getControl().getProperties().size(), is(2));
        assertThat(field.getControl().getProperties().get("attr"), is("extAttr"));
        assertThat(field.getControl().getProperties().get("attr2"), is("extAttr2"));

        field = (StandardField) rows.get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("field2"));
        assertThat(field.getProperties().size(), is(1));
        assertThat(field.getProperties().get(SECURITY_PROP_NAME), notNullValue());
        assertThat(field.getControl().getProperties() == null || field.getControl().getProperties().isEmpty(), is(true));

        field = (StandardField) rows.get(2).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("field3"));
        assertThat(field.getProperties() == null || field.getProperties().isEmpty(), is(true));
        assertThat(field.getControl().getProperties().size(), is(1));
        assertThat(field.getControl().getProperties().get("attr"), is("extAttr"));

        CustomField customField = (CustomField) rows.get(3).getCols().get(0).getFields().get(0);
        assertThat(customField.getId(), is("field4"));
        assertThat(customField.getProperties().size(), is(2));
        assertThat(customField.getProperties().get(SECURITY_PROP_NAME), notNullValue());
        assertThat(customField.getProperties().get("attr"), is("extAttr"));

        assertThat(customField.getControl().getProperties().size(), is(1));
        assertThat(customField.getControl().getProperties().get("attr2"), is("extAttr2"));
    }

}

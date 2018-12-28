package net.n2oapp.framework.config.metadata.compile.dependency;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга зависимости между полями
 */
public class FieldDependencyJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testJsonForm.object.xml"));
    }

    @Test
    public void testFieldDependency() {
        check("net/n2oapp/framework/config/metadata/compile/dependency/testFieldDependency.widget.xml",
                "core/dependencies/FieldDependency.meta.json")
                .cutJson("Page_Form.form")
                .exclude("fetchOnInit", "fieldDefaultValues", "filterDefaultValues",
                        "fieldsets[0].rows[0].cols[0].fields[0].control.readOnly",
                        "fieldsets[0].rows[0].cols[0].fields[0].control.disabled",
                        "fieldsets[0].rows[0].cols[0].fields[0].control.type",
                        "fieldsets[0].rows[0].cols[1].fields[0].control.readOnly",
                        "fieldsets[0].rows[0].cols[1].fields[0].control.disabled",
                        "fieldsets[0].rows[0].cols[1].fields[0].control.type",
                        "fieldsets[0].rows[1].cols[0].fields[0].control.readOnly",
                        "fieldsets[0].rows[1].cols[0].fields[0].control.disabled",
                        "fieldsets[0].rows[1].cols[0].fields[0].control.type",
                        "fieldsets[0].rows[1].cols[1].fields[0].control.readOnly",
                        "fieldsets[0].rows[1].cols[1].fields[0].control.disabled",
                        "fieldsets[0].rows[1].cols[1].fields[0].control.type",
                        "fieldsets[0].rows[2].cols[0].fields[0].control.readOnly",
                        "fieldsets[0].rows[2].cols[0].fields[0].control.disabled",
                        "fieldsets[0].rows[2].cols[0].fields[0].control.type",
                        "fieldsets[0].rows[2].cols[1].fields[0].control.readOnly",
                        "fieldsets[0].rows[2].cols[1].fields[0].control.disabled",
                        "fieldsets[0].rows[2].cols[1].fields[0].control.type",
                        "fieldsets[0].rows[2].cols[2].fields[0].control.readOnly",
                        "fieldsets[0].rows[2].cols[2].fields[0].control.disabled",
                        "fieldsets[0].rows[2].cols[2].fields[0].control.type",
                        "fieldsets[0].rows[3].cols[0].fields[0].control.readOnly",
                        "fieldsets[0].rows[3].cols[0].fields[0].control.disabled",
                        "fieldsets[0].rows[3].cols[0].fields[0].control.type",
                        "fieldsets[0].rows[3].cols[1].fields[0].control.readOnly",
                        "fieldsets[0].rows[3].cols[1].fields[0].control.disabled",
                        "fieldsets[0].rows[3].cols[1].fields[0].control.type",
                        "fieldsets[0].rows[3].cols[2].fields[0].control.readOnly",
                        "fieldsets[0].rows[3].cols[2].fields[0].control.disabled",
                        "fieldsets[0].rows[3].cols[2].fields[0].control.type",
                        "fieldsets[0].rows[1].cols[0].size",
                        "fieldsets[0].rows[1].cols[1].size",
                        "fieldsets[0].rows[2].cols[0].size",
                        "fieldsets[0].rows[2].cols[1].size",
                        "fieldsets[0].rows[2].cols[2].size",
                        "fieldsets[0].rows[3].cols[0].size",
                        "fieldsets[0].rows[3].cols[1].size",
                        "fieldsets[0].rows[3].cols[2].size",
                        "fieldsets[0].rows[3].cols[2].fields[0].dependency[1]",
                        "fieldsets[0].rows[3].cols[2].fields[0].dependency[0].applyOnInit",
                        "modelPrefix"
                )
                .cutXml("form")
                .assertEquals();
    }
}

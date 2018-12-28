package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Тест тестера правильных сборок json метаданных из xml
 */
@Ignore //todo
public class JsonMetadataTesterTest extends JsonMetadataTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack());
    }

    @Test
    public void test() {
        check("net/n2oapp/framework/config/selective/testjson.page.xml",
                "net/n2oapp/framework/config/selective/testjson.json")
                .cutXml("regions.single[0].containers[0].widget.cells[0]")
                .assertEquals();
    }
}

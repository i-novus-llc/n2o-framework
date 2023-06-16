package net.n2oapp.framework.config.metadata.validation.datasource;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.ApplicationDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирования валидации источника данных, ссылающегося на источник из application.xml
 */
public class ApplicationDatasourceValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIOv3());
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(), new ApplicationDatasourceValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/empty.application.xml"));
        builder.properties("n2o.application.id=empty");
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующий источник данных из application.xml
     */
    @Test
    void testNonExistentObject() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testApplicationDatasourceNonExistentId.page.xml"),
                "Источник данных <app-datasource> ссылается на несуществующий в empty.application.xml источник данных 'nonEXIST'"
        );
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующий источник данных из application.xml
     */
    @Test
    void testSourceDatasourceNonExistentId() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testSourceAppDatasourceNonExistentId.page.xml"),
                "Источник данных <app-datasource> ссылается на несуществующий в empty.application.xml источник данных 'nonEXIST'"
        );
    }
}

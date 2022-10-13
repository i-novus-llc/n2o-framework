package net.n2oapp.framework.config.metadata.validation.datasource;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.InheritedDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации источника данных, получающего данные из другого источника данных
 */
public class InheritedDatasourceValidatorTest extends SourceValidationTestBase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(), new InheritedDatasourceValidator());
    }

    /**
     * Проверяется наличие datasource, на который ссылается атрибут 'source-datasource'
     */
    @Test
    public void testSourceDatasourceLink() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут 'source-datasource' источника данных 'inh1' ссылается на несуществующий источник 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistSourceDatasource.page.xml");
    }

    /**
     * Проверяется, что действие submit внутри источника данных ссылается на существующий источник данных
     */
    @Test
    public void testTargetDatasourceLink() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут 'target-datasource' элемента 'submit' источника данных 'inh1' ссылается на несуществующий источник 'ds2'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistTargetDatasource.page.xml");
    }

}

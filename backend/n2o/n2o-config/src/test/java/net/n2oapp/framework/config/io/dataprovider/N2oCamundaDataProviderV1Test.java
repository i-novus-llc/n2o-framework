package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения\записи Camunda провайдера данных
 */
public class N2oCamundaDataProviderV1Test {
    
    @Test
    void io() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new CamundaDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oCamundaProviderIOTest.xml");
    }
}

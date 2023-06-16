package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения\записи MongoDb провайдера данных
 */
public class N2oMongoDbDataProviderV1Test {
    
    @Test
    void io() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new MongoDbDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oMongoDbProviderIOTest.xml");
    }
}

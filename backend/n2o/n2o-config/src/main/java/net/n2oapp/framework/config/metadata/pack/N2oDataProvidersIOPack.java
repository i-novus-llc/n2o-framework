package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.io.dataprovider.JavaDataProviderIOv1;
import net.n2oapp.framework.config.io.dataprovider.RestDataProviderIOv1;
import net.n2oapp.framework.config.io.dataprovider.SqlDataProviderIOv1;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;

/**
 * Набор считывателей провайдеров данных
 */
public class N2oDataProvidersIOPack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.ios(new RestDataProviderIOv1(),
                new SqlDataProviderIOv1(),
                new JavaDataProviderIOv1(),
                new TestDataProviderIOv1());
    }
}

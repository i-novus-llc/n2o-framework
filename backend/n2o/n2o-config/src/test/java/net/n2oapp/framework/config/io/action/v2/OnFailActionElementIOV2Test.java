package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.config.metadata.pack.N2oAllIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Проверка чтения и персиста события <on-fail>
 */
class OnFailActionElementIOV2Test {

    @Test
    void testOnFailActionElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.packs(new N2oAllIOPack());
        tester.ios(new InvokeActionElementIOV2(), new OnFailActionElementIOV2(), new AlertActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/action/v2/testOnFailActionElementIOV2.page.xml");
    }
}
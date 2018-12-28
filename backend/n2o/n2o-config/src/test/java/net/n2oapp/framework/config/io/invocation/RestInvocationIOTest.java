package net.n2oapp.framework.config.io.invocation;

import net.n2oapp.framework.api.metadata.global.dao.RestErrorMapping;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oRestInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.config.persister.invocation.N2oRestInvocationPersister;
import net.n2oapp.framework.config.reader.invocation.RestInvocationReaderV2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/*
 * @author enuzhdina
 * @since 16.06.2015.
 */
public class RestInvocationIOTest {

    @Test
    public void testRestInvocationIOV2(){
        SelectiveReader reader = new SelectiveStandardReader()
                .addObjectReader().addInvocationsReader2();

        SelectivePersister persister = new SelectiveStandardPersister()
                .addObjectPersister()
                .addPersister(new N2oRestInvocationPersister());

        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(reader)
                .addPersister(persister);

        assert tester.check("net/n2oapp/framework/config/io/invocation/testRestInvocationReaderV2.object.xml",
                (N2oObject object) -> {
        assert object.getOperations().length == 1;
        N2oRestInvocation restInvocation = (N2oRestInvocation) object.getOperations()[0].getInvocation();
        assertBase(restInvocation);

        RestErrorMapping restErrorMapping = restInvocation.getErrorMapping();
        assert restErrorMapping.getMessage().equals("test");
        assert restErrorMapping.getDetailedMessage().equals("test");
        assert restErrorMapping.getDetailedMessage().equals("test");

        assert restInvocation.getQuery().equals("test");
        });
    }

    private void assertBase(N2oRestInvocation restInvocation){
        assert restInvocation.getDateFormat().equals("test");
        assert restInvocation.getMethod().equals("DELETE");
        assert restInvocation.getProxyHost().equals("test");
        assert restInvocation.getProxyPort().equals(5432);

    }
}

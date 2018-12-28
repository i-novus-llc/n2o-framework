package net.n2oapp.framework.config.io.invocation;

import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.config.persister.invocation.N2oSqlInvocationPersister;
import net.n2oapp.framework.config.reader.invocation.SqlInvocationReaderV2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class ActionParametersIOTest {
    private SelectiveReader reader = new SelectiveStandardReader()
            .addObjectReader()
            .addReader(new SqlInvocationReaderV2());

    private SelectivePersister persister = new SelectiveStandardPersister()
            .addObjectPersister()
            .addPersister(new N2oSqlInvocationPersister());

    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(reader)
            .addPersister(persister);

    @Test
    public void testSqlInvocationIO() {
        assert tester.check("net/n2oapp/framework/config/io/invocation/testActionParametersIO.object.xml",
                (N2oObject object) -> {
                    assert object.getOperations().length == 1;
                    assert object.getOperations()[0].getInParameters().length == 4;
                    assert object.getOperations()[0].getInParameters()[3].getNormalize().equals("snils.replaceAll('-', '')");
                });
    }
}

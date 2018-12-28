package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGrid;
import net.n2oapp.framework.config.persister.control.N2oCheckboxGridPersister;
import net.n2oapp.framework.config.reader.control.N2oCheckboxGridXmlReaderV1;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Created by schirkova on 13.05.2015.
 */
public class N2oCheckboxGridReaderTest {
    @Test
    public void test() throws Exception {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addReader(new N2oCheckboxGridXmlReaderV1());
        tester.addPersister(new N2oCheckboxGridPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/checkboxgrid.xml", (N2oCheckboxGrid n2o) -> {
            assert n2o.getId().equals("checkboxGrid");
            assert n2o.getLabel().equals("Множественный выбор из таблицы");
            assert n2o.getQueryId().equals("gender");
            assert n2o.getLabelFieldId().equals("name");
            assert n2o.getValueFieldId().equals("id");
            assert n2o.getColumns()[0].getColumnFieldId().equals("id");
            assert n2o.getColumns()[0].getName().equals("Ид.");
            assert n2o.getColumns()[1].getColumnFieldId().equals("name");
        });
    }

}

package net.n2oapp.framework.config.io.control.v3.filters_buttons;

import net.n2oapp.framework.config.io.fieldset.v5.SetFieldsetElementIOv5;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

public class ClearButtonIOv3Test {

    @Test
    void testButton() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .ios(new TableElementIOV5(), new SetFieldsetElementIOv5(), new ClearButtonIOv3());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/filters_buttons/testClearButtonIOv3.widget.xml");
    }
}

package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.test.N2oTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class CheckFieldIdTest extends N2oTestBase {

    N2oCompileProcessor processor;
    N2oInputText field;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        processor = new N2oCompileProcessor(builder.getEnvironment());
        field = new N2oInputText();
    }

    @Test
    public void testFieldId() {
        checkId(null);
        checkId("_");
        checkId("a");
        checkId("a_1_2");
        checkId("a[0]");
        checkId("a*.name");

        checkWrongId("");
        checkWrongId("1");
        checkWrongId("*");
        checkWrongId("*a");
        checkWrongId("*[0]");
        checkWrongId("123");
        checkWrongId("switch");
        checkWrongId("default");
        checkWrongId("true");
        checkWrongId("поле");
    }

    private void checkWrongId(String id) {
        try {
            checkId(id);
            fail(String.format("id '%s' should be incorrect!", id));
        } catch(N2oMetadataValidationException e) {
        }
    }

    private void checkId(String id) {
        field.setId(id);
        processor.checkId(field, "");
    }
}

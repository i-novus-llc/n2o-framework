package net.n2oapp.framework.sandbox.cases.case24;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TestDynamicProvider implements DynamicMetadataProvider {

    @Override
    public String getCode() {
        return "testDynamic";
    }

    @Override
    public List<? extends SourceMetadata> read(String context) {
        N2oSimplePage page = new N2oSimplePage();
        N2oForm form = new N2oForm();
        form.setRefId("version" + context);
        form.setQueryId("dynamic");
        form.setObjectId("testDynamic?" + context);
        page.setWidget(form);

        N2oObject n2oObject = new N2oObject();
        n2oObject.setName("testDynamic?" + context);
        final N2oObject.Operation operation = new N2oObject.Operation();
        operation.setId("test");
        final N2oTestDataProvider invocation = new N2oTestDataProvider();
        invocation.setOperation(N2oTestDataProvider.Operation.echo);
        operation.setInvocation(invocation);
        n2oObject.setOperations(new N2oObject.Operation[]{operation});

        return Arrays.asList(page, n2oObject);
    }
}

package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.BulkOperationUtils;
import org.springframework.stereotype.Controller;

import java.util.Collection;

/**
 * User: iryabov
 * Date: 10.05.13
 * Time: 10:58
 */
@Controller
public class BulkActionMergeController extends BulkActionController {


    public BulkActionMergeController(DataProcessingStack dataProcessingStack,
                                     N2oOperationProcessor actionProcessor,
                                     AlertMessageBuilder messageBuilder,
                                     MetadataEnvironment environment) {
        super(dataProcessingStack, actionProcessor, messageBuilder, environment);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SetDataResponse execute(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo) {
        DataSet updateInfo = (DataSet) requestInfo.getData();
        Collection<DataSet> modelList = (Collection<DataSet>) updateInfo.get(BulkOperationUtils.BULK_MODEL_FIELD);
        updateInfo.remove(BulkOperationUtils.BULK_MODEL_FIELD);
        modelList.forEach(d -> d.merge(updateInfo));
        requestInfo.setData(modelList);
        requestInfo.setBulk(true);
        return super.execute(requestInfo, responseInfo);
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.bulkMergeOperation;
    }
}

package net.n2oapp.framework.engine.validation;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.processing.N2oModule;
import net.n2oapp.framework.api.ui.*;
import net.n2oapp.framework.engine.validation.engine.FailInfo;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.engine.validation.engine.info.ObjectValidationInfo;
import net.n2oapp.framework.engine.validation.engine.info.QueryValidationInfo;

import java.util.Collection;
import java.util.List;

import static net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation.ServerMoment.*;

/**
 * Процессинговый модуль валидации
 */
public class N2oValidationModule extends N2oModule {

    private ValidationProcessor processor;

    public N2oValidationModule(ValidationProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), beforeOperation);
        prepareResponse(fails, responseInfo);
    }

    @Override
    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet, N2oException exception) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), afterFailOperation);
        prepareResponse(fails, responseInfo);
    }

    @Override
    public void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), afterSuccessOperation);
        prepareResponse(fails, responseInfo);
    }

    @Override
    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
        if (requestInfo.isValidationEnable() && requestInfo.getSize() == 1) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, requestInfo.getData()), afterFailQuery);
            prepareResponse(fails, responseInfo);
        }
    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        if (requestInfo.isValidationEnable() && requestInfo.getSize() != 1) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, requestInfo.getData()), beforeQuery);
            prepareResponse(fails, responseInfo);
        }
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        final Collection<DataSet> list = page.getCollection();
        if (requestInfo.isValidationEnable() && !list.isEmpty() && requestInfo.getSize() == 1) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, list.iterator().next()), beforeOperation);
            prepareResponse(fails, responseInfo);
        }
    }

    private ObjectValidationInfo buildInfo(ActionRequestInfo<DataSet> requestInfo, DataSet dataSet) {
        return new ObjectValidationInfo(
                requestInfo.getObject().getId(),
                requestInfo.getOperation().getValidationList(),
                dataSet,
                requestInfo.getFailAlertWidgetId(),
                requestInfo.getMessagesForm()
        );
    }

    private QueryValidationInfo buildInfo(QueryRequestInfo requestInfo, DataSet dataSet) {
        return new QueryValidationInfo(
                requestInfo.getQuery().getObject(),
                requestInfo.getQuery().getValidations(),
                dataSet != null ? dataSet : requestInfo.getData(),
                requestInfo.getFailAlertWidgetId(),
                requestInfo.getMessagesForm()
        );
    }

    private void prepareResponse(List<FailInfo> fails, ResponseInfo responseInfo) {
        for (FailInfo fail : fails) {
            ResponseMessage message = new ResponseMessage();
            message.setText(fail.getMessage());
            message.setField(fail.getFieldId());
            message.setSeverityType(fail.getSeverity());
            responseInfo.addMessage(message);
        }
    }
}

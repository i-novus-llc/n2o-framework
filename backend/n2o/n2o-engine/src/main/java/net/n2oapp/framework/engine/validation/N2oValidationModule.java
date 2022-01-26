package net.n2oapp.framework.engine.validation;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.*;
import net.n2oapp.framework.engine.validation.engine.FailInfo;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.engine.validation.engine.info.ObjectValidationInfo;
import net.n2oapp.framework.engine.validation.engine.info.QueryValidationInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation.ServerMoment.*;

/**
 * Процессинговый модуль валидации
 */
public class N2oValidationModule implements DataProcessing {

    private ValidationProcessor processor;

    public N2oValidationModule(ValidationProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), beforeOperation);
        prepareResponse(fails, requestInfo, responseInfo);
    }

    @Override
    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), afterFailOperation);
        prepareResponse(fails, requestInfo, responseInfo);
    }

    @Override
    public void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), afterSuccessOperation);
        prepareResponse(fails, requestInfo, responseInfo);
    }

    @Override
    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
        if (requestInfo.isValidationEnable() && requestInfo.getSize() == 1) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, requestInfo.getData()), afterFailQuery);
            prepareResponse(fails, requestInfo, responseInfo);
        }
    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        if (requestInfo.isValidationEnable()) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, requestInfo.getData()), beforeQuery);
            prepareResponse(fails, requestInfo, responseInfo);
        }
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        final Collection<DataSet> list = page.getCollection();
        if (requestInfo.isValidationEnable() && !list.isEmpty()) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, list.iterator().next()), afterSuccessQuery);
            prepareResponse(fails, requestInfo, responseInfo);
        }
    }

    private ObjectValidationInfo buildInfo(ActionRequestInfo<DataSet> requestInfo, DataSet dataSet) {
        return new ObjectValidationInfo(
                requestInfo.getObject().getId(),
                requestInfo.getOperation().getValidationList(),
                dataSet,
                requestInfo.getMessagesForm()
        );
    }

    private QueryValidationInfo buildInfo(QueryRequestInfo requestInfo, DataSet inDataSet) {
        DataSet dataSet = inDataSet != null ? inDataSet : requestInfo.getData();
        DataSet result = new DataSet();
        Map<String, String> paramsMap = requestInfo.getQuery().getParamToFilterIdMap();
        for (String key : dataSet.keySet()) {
            if (paramsMap.containsKey(key)) {
                if (DataSet.isSpreadKey(paramsMap.get(key)) && !(dataSet.get(key) instanceof Collection))
                    result.put(paramsMap.get(key), Collections.singletonList(dataSet.get(key)));
                else
                    result.put(paramsMap.get(key), dataSet.get(key));
            } else
                result.put(key, dataSet.get(key));
        }
        return new QueryValidationInfo(
                requestInfo.getQuery().getObject(),
                requestInfo.getQuery().getValidations(),
                result,
                requestInfo.getMessagesForm()
        );
    }

    private void prepareResponse(List<FailInfo> fails, RequestInfo requestInfo, ResponseInfo responseInfo) {
        for (FailInfo fail : fails) {
            ResponseMessage message = responseInfo.constructMessage(requestInfo, fail.getSeverity());
            message.setText(fail.getMessage());
            message.setField(fail.getFieldId());
            responseInfo.addMessage(message);
        }
    }
}

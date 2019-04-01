package net.n2oapp.framework.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.*;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.Map;
import java.util.Set;

/**
 * Контроллер данных
 */
public class DataController extends AbstractController {

    private ControllerFactory controllerFactory;

    public DataController(ControllerFactory controllerFactory,
                          ObjectMapper mapper,
                          MetadataRouter router,
                          MetadataEnvironment environment) {
        setObjectMapper(mapper);
        setRouter(router);
        setEnvironment(environment);
        this.controllerFactory = controllerFactory;
        setErrorMessageBuilder(new ErrorMessageBuilder(environment.getMessageSource()));
    }

    public GetDataResponse getData(String path, Map<String, String[]> parameters, UserContext user) {
        QueryRequestInfo requestInfo = createQueryRequestInfo(path, parameters, user);
        QueryResponseInfo responseInfo = new QueryResponseInfo();
        GetDataResponse result = controllerFactory.execute(requestInfo, responseInfo);
        handleError(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public SetDataResponse setData(String path, Map<String, String[]> parameters, Object body, UserContext user) {
        ActionRequestInfo requestInfo = createActionRequestInfo(path, parameters, body, user);
        ActionResponseInfo responseInfo = new ActionResponseInfo();
        SetDataResponse result = controllerFactory.execute(requestInfo, responseInfo);
        resolveRedirect(requestInfo, result);
        handleError(result);
        return result;
    }

    private void resolveRedirect(ActionRequestInfo requestInfo, SetDataResponse response) {
        if (requestInfo.getRedirect() == null)
            return;
        RedirectSaga redirect = requestInfo.getRedirect();
        DataSet data = new DataSet(response.getData());
        data.merge(requestInfo.getQueryData());
        String redirectUrl = redirect.getPath();
        Set<String> except = redirect.getPathMapping() != null ? redirect.getPathMapping().keySet() : null;
        redirectUrl = RouteUtil.resolveUrlParams(redirectUrl, requestInfo.getQueryData(), null, except);
        redirectUrl = RouteUtil.resolveUrlParams(redirectUrl, response.getData(), null, except);
        RedirectSaga resolvedRedirect = new RedirectSaga();
        resolvedRedirect.setTarget(redirect.getTarget());
        resolvedRedirect.setPathMapping(redirect.getPathMapping());
        resolvedRedirect.setQueryMapping(redirect.getQueryMapping());
        resolvedRedirect.setPath(redirectUrl);
        response.addRedirect(resolvedRedirect);
    }
}

package net.n2oapp.framework.api.ui;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * User: operhod
 * Date: 17.07.14
 * Time: 15:17
 */
public class ActionRequestInfo<D> extends RequestInfo {

    //immutable
    private CompiledObject object;
    private CompiledObject.Operation operation;
    private String choice;
    private boolean isBulk;
    private RedirectSaga redirect;

    //mutable
    private Map<String, N2oObject.Parameter> inParametersMap = new LinkedHashMap<>();
    private Map<String, N2oObject.Parameter> outParametersMap = new LinkedHashMap<>();
    /**
     * "Сырые" данные, не приведенные к домену
     */
    private D data;

    public void setObject(CompiledObject object) {
        this.object = object;
    }

    public void setOperation(CompiledObject.Operation operation) {
        this.operation = operation;
        if (operation.getInParametersMap() != null)
            for (String paramName : operation.getInParametersMap().keySet()) {
                N2oObject.Parameter srcParam = operation.getInParametersMap().get(paramName);
                inParametersMap.put(paramName, new N2oObject.Parameter(srcParam));
            }
        if (operation.getOutParametersMap() != null)
            for (String paramName : operation.getOutParametersMap().keySet()) {
                N2oObject.Parameter srcParam = operation.getOutParametersMap().get(paramName);
                outParametersMap.put(paramName, new N2oObject.Parameter(srcParam));
            }
    }

    public Map<String, N2oObject.Parameter> getInParametersMap() {
        return inParametersMap;
    }

    public Map<String, N2oObject.Parameter> getOutParametersMap() {
        return outParametersMap;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setBulk(boolean isBulk) {
        this.isBulk = isBulk;
    }

    public void setData(D data) {
        this.data = data;
    }

    public boolean isBulk() {
        return data instanceof Collection || isBulk;
    }

    public D getData() {
        return data;
    }

    public String getChoice() {
        return choice;
    }

    public CompiledObject getObject() {
        return object;
    }

    public CompiledObject.Operation getOperation() {
        return operation;
    }


    public boolean isValidationEnable() {
        return getOperation() != null && getOperation().isReal() && getOperation().isValidationEnable();
    }


    /**
     * Если bulk, то разбиваем на более простые "запросы"
     */
    @SuppressWarnings("unchecked")
    public List<ActionRequestInfo<DataSet>> toList() {
        if (!isBulk() || getData() == null)
            return emptyList();
        return ((Collection<DataSet>) getData())
                .stream()
                .map(dataSet -> {
                    ActionRequestInfo<DataSet> info = new ActionRequestInfo();
                    info.setData(dataSet);
                    info.setObject(getObject());
                    info.setOperation(getOperation());
                    info.setUser(getUser());
                    //важно помнить что request bulk!
                    info.setBulk(true);
                    info.setChoice(getChoice());
                    return info;
                })
                .collect(Collectors.toList());
    }

    public RedirectSaga getRedirect() {
        return redirect;
    }

    public void setRedirect(RedirectSaga redirect) {
        this.redirect = redirect;
    }
}
